const assert = require('assert');
const { chromium }  = require('playwright');

(async () => 
  {
    const browser = await chromium.launch({ headless: false });
    await test_first_100_articles_are_sorted(browser);
    browser.close();
  }) ();

async function test_first_100_articles_are_sorted(browser) {
  const page_of_articles = await sort_articles_by_newest(browser);
  const rows_encountered = await validate_articles_are_sorted(page_of_articles);
  await verify_exactly_101_rows_were_encountered(rows_encountered);
}

async function sort_articles_by_newest(browser) {
  const context = await browser.newContext();
  const page_of_articles = await context.newPage();
  await page_of_articles.goto("https://news.ycombinator.com/newest");

  return page_of_articles;
}

async function validate_articles_are_sorted(page_of_articles) {
  const rows_of_articles = page_of_articles.locator('tr.athing');
  const rows_encountered = await compare_times_of_articles(page_of_articles, rows_of_articles);
  return rows_encountered;
}

const path_to_adjacent_sibling = '+ tr';
const path_to_time_entry = '.subtext .subline .age';
const path_to_rank = '.rank';
const attribute_for_time = 'title';

async function compare_times_of_articles(page_of_articles, rows_of_articles) {  
  const row_count = await rows_of_articles.count();
  for (let index = 0; index < row_count - 1; index++) {
    const total_rows_encountered = await read_row_number(rows_of_articles, index);
    if (total_rows_encountered > 100) {
      return total_rows_encountered;
    } else {
      await check_rows_with_indices(rows_of_articles, index, index + 1);
    }
  }
  await proceed_to_more_variables(page_of_articles, rows_of_articles);
  return await compare_times_of_articles(page_of_articles, rows_of_articles);
}

async function read_row_number(rows_of_articles, index) {
  const current_row = rows_of_articles.nth(index);
  const text = await current_row.locator(path_to_rank).innerText();
  const text_without_period = text.replace('.', '');

  const DECIMAL_RADIX = 10;
  const rank = parseInt(text_without_period, DECIMAL_RADIX);
  return rank;
}

async function check_rows_with_indices(rows_of_articles, index_1, index_2) {
  const previous_title_row = rows_of_articles.nth(index_1);
  const next_title_row = rows_of_articles.nth(index_2);

  const previous_subtext_row = previous_title_row.locator(path_to_adjacent_sibling);
  const next_subtext_row = next_title_row.locator(path_to_adjacent_sibling);

  const previous_age_locator = previous_subtext_row.locator(path_to_time_entry);
  const next_age_locator = next_subtext_row.locator(path_to_time_entry);
  
  const previous_time = await previous_age_locator.getAttribute(attribute_for_time);
  const next_time = await next_age_locator.getAttribute(attribute_for_time);

  assert(previous_time >= next_time);
}

async function proceed_to_more_variables(page_of_articles) {
  const more_rows = page_of_articles.getByRole('link', { name: 'More', exact: true});
  await more_rows.click();
}

async function verify_exactly_101_rows_were_encountered(rows_verified) {
  if (rows_verified == 101) {
    console.log(`✅ Exactly 100 rows were verified.`);
  } else {
    console.log(`❌ Exactly 100 rows were not verified.`);
    console.log(` Actual number of rows verified: ${rows_verified}.`);
  }
}
