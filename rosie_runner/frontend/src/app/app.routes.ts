import { Routes } from '@angular/router';
import { TitleScreenComponent } from './title-screen/title-screen.component';
import { SignUpComponent } from './sign-up/sign-up.component';
import { LogInComponent } from './log-in/log-in.component';
import { GameComponent } from './game/game.component';
import { DeleteAccountComponent } from './delete-account/delete-account.component';
import { LeaderboardComponent } from './leaderboard/leaderboard.component';
import { authenticationGuard } from './authentication/authentication.guard';


export const routes: Routes = [
    { path: '', component: TitleScreenComponent },
    { path: 'log-in', component: LogInComponent},
    { path: 'sign-up', component: SignUpComponent},
    { path: 'game', component: GameComponent, canActivate: [authenticationGuard]},
    {path: 'delete-account', component: DeleteAccountComponent},
    {path: 'leaderboard', component: LeaderboardComponent},
];



