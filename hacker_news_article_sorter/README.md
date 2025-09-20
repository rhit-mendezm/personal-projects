# Playwright Test

A JavaScript testing program using [Microsoft’s Playwright framework](https://playwright.dev/docs/intro) to **validate that exactly 100
articles are sorted from newest to oldest** on Hacker News. This project was developed as part of a hiring assessment for a quality 
assurance organization.

## Features

* Confirms articles are sorted from newest to oldest.
* Uses Playwright to interact with the web page dynamically.
* Resilient against changes to the website's DOM.
* Outputs pass/fail message to the console.
* Visually displays progress in a Chromium browser tab.

## Installation and Testing

Clone the repository and install dependencies:

```bash
git clone git@github.com:rhit-mendezm/personal-projects.git

cd hacker_news_article_sorter

npm i

node index.js
```
**Notes**: 
* You may need to install npm (node package manager) on your machine first.
* The `node_modules` dependency folder can be removed with `rm -rf node_modules/`.
* You may be prompted to run `npx install playwright`, so the chromium tab can open.

