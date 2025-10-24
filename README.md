# CST2335 – Lab 6: Android TV Cat Viewer

Android TV app that fetches random cat images from Cataas, shows a progress bar while loading, and caches images locally.

## Emulator
- Device: Android TV (1080p), API 36
- Resolution: 1920×1080, 320 dpi

## Tech
- Manifest: INTERNET, leanback features, LEANBACK_LAUNCHER
- Theme: MaterialComponents.DayNight.NoActionBar
- AsyncTask loop: fetch JSON → load/download image → cache by id → update ImageView + ProgressBar

## Screenshots
See the /screenshots folder.
