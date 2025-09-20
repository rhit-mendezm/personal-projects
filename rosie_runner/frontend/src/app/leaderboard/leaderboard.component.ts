import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { environment } from '../../environments/environment.prod';


interface LeaderboardEntry {
  rank: number;
  username: string;
  major: string;
  score: number;
  time: string;
}

@Component({
  selector: 'app-leaderboard',
  imports: [ReactiveFormsModule, RouterModule, CommonModule],
  templateUrl: './leaderboard.component.html',
  styleUrl: './leaderboard.component.css'
})
export class LeaderboardComponent implements OnInit {
  leaderboard: LeaderboardEntry[] = [];
  username: string = '';

  constructor(
    private http: HttpClient,
  ) {}

  ngOnInit(): void {
    this.username = sessionStorage.getItem('username')!;
    this.fetchLeaderboard();
  }

  fetchLeaderboard() {
  this.http.get<{[key: string]: Omit<LeaderboardEntry, 'rank'>}>(`${environment.backendURL}/leaderboard`).subscribe({
    next: (data) => {
      const leaderboardArray: Omit<LeaderboardEntry, 'rank'>[] = Object.values(data);

      leaderboardArray.sort((entry1, entry2) => {
        if (entry2.score !== entry1.score) {
          return entry2.score - entry1.score;
        }
        return entry1.time.localeCompare(entry2.time);
      });

      this.leaderboard = leaderboardArray.map((entry, index) => ({
        ...entry,
        rank: index + 1,
        time: this.formatTime(entry.time)
      }));
    },
    error: (error) => {
      console.error(`Error fetching leaderboard data: ${error}`);
      this.leaderboard = [];
    }
  });
  }

  formatTime(isoDate: string)  {
    const date = new Date(isoDate);
    const options: Intl.DateTimeFormatOptions = { year: 'numeric', month: 'long', day: 'numeric' };
    return date.toLocaleDateString(undefined, options);
  }

  updateLeaderboard(username: string, score: number) {
    const data = {username, score};
    this.http.post<LeaderboardEntry[]>(`${environment.backendURL}/leaderboard`, data).subscribe({
      next: (data) => {
        this.leaderboard = data;
      },
      error: (error) => {
        console.error(`Error updating leaderboard data: ${error}`);
        this.leaderboard = [];
      }
    });
  }
}
