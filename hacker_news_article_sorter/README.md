# Playwright Test

Developed by Microsoft, [Playwright](https://playwright.dev/) is a testing framework focused on validating
frontend displays. In this project, I used JavaScript to validate that **Exactly 100 articles are sorted 
from newest to oldest** on [Hacker News](https://news.ycombinator.com/).

I wrote this program as part of a hiring assessment for a quality assurance organization. Since I am proud
of my code, I am displaying it in my portfolio.

## Problem Description

Using the Playwright framework, compose a test that confirms that exactly 100 articles are sorted from newest
to oldest. There are several solutions, and I approached this problem with the goal of making a readable test
that is resilient against changes to the DOM of the website.

## Test Execution
1. Clone this project to your machine.
2. In the directory where this project is cloned, install node modules by running `npm i`.
3. Run the test with the command `node index.js`.

Note: The program will briefly open a Chromium tab to visually display progress. The console will also output a
pass/fail message.

