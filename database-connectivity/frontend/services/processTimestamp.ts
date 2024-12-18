export function formatTimeDifference(timestamp: string): string {
  const now = Date.now();
  const difference = now - Date.parse(timestamp) - 14400000;

  // Calculate the time difference in seconds, minutes, hours, and days
  const seconds = Math.floor(difference / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  const days = Math.floor(hours / 24);

  if (days > 30) {
    // If the time difference is more than 30 days, return the month difference
    const months = Math.floor(days / 30);
    return `posted ${months} month${months > 1 ? "s" : ""} ago`;
  } else if (days > 0) {
    // If the time difference is more than 0 days, return the day difference
    return `posted ${days} day${days > 1 ? "s" : ""} ago`;
  } else if (hours > 0) {
    // If the time difference is more than 0 hours, return the hour difference
    return `posted ${hours} hour${hours > 1 ? "s" : ""} ago`;
  } else if (minutes > 0) {
    // If the time difference is more than 0 minutes, return the minute difference
    return `posted ${minutes} minute${minutes > 1 ? "s" : ""} ago`;
  } else {
    // If the time difference is less than 1 minute, return "just now"
    return "just now";
  }
}
