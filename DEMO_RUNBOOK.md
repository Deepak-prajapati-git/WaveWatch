# Wave Watch - Demo Runbook for Judges

## Welcome to Wave Watch
Wave Watch is a production-polish Android maritime telemetry and emergency safety app built with Jetpack Compose, Material 3, Room, DataStore, and Custom Canvas instruments.

---

## Step 1: Launch & First-Run Onboarding
1. Install and launch the app on an emulator or physical device.
2. Observe the Android 12+ Splash screen and smooth entry into the **Onboarding Pager**.
3. Swipe through the 3 onboarding slides highlighting:
   - Advanced Marine Telemetry
   - Emergency SOS Beacon (Interactive 3s hold demo)
   - Tactical Radar & Geofence Protection
4. Grant runtime Location, Bluetooth, and Notification permissions when prompted by the **PermissionGate**.
5. Tap **"LAUNCH WAVE WATCH"** to enter the main app.

## Step 2: Explore the Dashboard
1. You will land on the **DashboardScreen**.
2. Notice the Abyssal Navy theme (`#0B131F`), Neon Cyan accents, and monospace tabular typography.
3. Observe the live **Artificial Horizon Gyroscope** rotating and pitching in real-time.
4. Check the **Swell Spectrum Waveform** rendering glowing animated dual sine waves.
5. Review the 2x2 **Metric Glass Cards** displaying Wave Height, Vessel Speed, Water Temp, and Gyro Pitch/Roll with smooth animated content transitions.

## Step 3: Test the Judge Demo Controller
1. Tap the **Demo** tab in the bottom navigation bar.
2. Locate the **Judge Demo Controller** card at the top.
3. **Trigger Calm Sea**: Watch wave height drop to 0.8m and status turn Success Green.
4. **Trigger Rough Weather**: Watch wave height surge to 4.8m, pitch/roll spike, and alert level shift to ROUGH / CRITICAL with Crimson Red warning.
5. **Trigger Border Breach**: Move GPS coordinates near the International Maritime Boundary with immediate CRITICAL geofence alert.
6. **Simulate GPS Dropout**: Watch GPS status indicator switch to red "GPS SIGNAL LOST" with 0.0 coordinates.
7. **Simulate Hardware Disconnect**: Watch telemetry zero out and hardware status indicate disconnection.

## Step 4: Explore Tactical Radar & Geofence Map
1. Tap the **Radar** tab in the bottom navigation bar.
2. View the tactical dark radar grid with concentric distance rings, crosshairs, and vessel wake trail.
3. Observe proximity hazard markers (Colaba Reef Shallows, International Maritime Boundary, Tanker Traffic Lane) plotted relative to vessel heading.

## Step 5: Test Emergency SOS Rescue Flow
1. Tap the red **SOS** tab in the bottom navigation bar.
2. Press and hold the circular crimson **72dp+ HoldToActivateSosButton** for 3 seconds.
3. Feel the haptic feedback tick and watch the 3s circular hold progress ring fill up.
4. Upon completion, observe the **Emergency Beacon Active** state with packet ID, coordinates, and RF mesh relay status.
5. Tap **"CANCEL / RESOLVE RESCUE"** to resolve the distress protocol and return to normal monitoring.
