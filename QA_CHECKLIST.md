# Wave Watch - QA Checklist & Verification

## 1. Gradle Setup & Dependencies
- [x] Room database (`room-runtime`, `room-ktx`, `room-compiler`) successfully configured and synced with KSP.
- [x] DataStore Preferences configured for user preferences persistence.
- [x] Navigation Compose, Lifecycle ViewModel Compose, and Material Icons Extended integrated.
- [x] Core Splashscreen and Coroutines Android setup completed.

## 2. Theme & Typography
- [x] Abyssal Navy theme (`#0B131F`) with Neon Cyan (`#00E5FF`), Amber (`#FFB300`), Crimson Red (`#FF385C`), Success Green (`#34D399`), Muted Text (`#8CA0B8`).
- [x] Monospace tabular typography applied across data displays.
- [x] WCAG AA color contrast ratios verified for marine outdoor visibility.

## 3. Live Sensor & Telemetry Simulator Engine
- [x] 250ms background coroutine emitting realistic sinusoidal wave height, period, pitch, roll, and GPS drift near Mumbai (18.9° N, 72.8° E).
- [x] Smooth 1.5s eased transitions for simulation presets (Calm Sea, Rough Weather, Border Breach, GPS Dropout, Hardware Disconnect).

## 4. Custom Compose Canvas Instruments & UI
- [x] `ArtificialHorizonView.kt`: Gyroscope indicator with roll rotation, pitch translation, and dynamic alert ring color shifting.
- [x] `WaveSwellWaveformView.kt`: Animated dual sine wave canvas path with glowing gradient fill and parallax.
- [x] `MetricGlassCard.kt`: Glassmorphism cards with animated content transitions and trend indicators.
- [x] `HoldToActivateSosButton.kt`: 72dp+ circular crimson button with 3s hold progress ring, haptics, and discoverable semantics.

## 5. Screens & Navigation
- [x] Dashboard Screen with GPS status, hero banner, canvas instruments, metric grid, and staleness timestamp.
- [x] Tactical Radar Map with concentric geofence rings, boat wake trail, and proximity HUD.
- [x] SOS Center with distress protocol, RF/BLE relay status, countdown overlay, and cancellation flow.
- [x] Diagnostics & Demo Controller screen with judge preset triggers, BLE scanner status, and unit system toggles.
- [x] Bottom Navigation Bar with distinct red SOS icon and edge-to-edge layout.

## 6. Onboarding & Permissions
- [x] 3-slide Onboarding Pager gated by `UserPreferences.hasCompletedOnboarding`.
- [x] Reusable `PermissionGate` handling runtime location, Bluetooth, and notification permissions.

## 7. Persistence & Background Reliability
- [x] Room database logging telemetry and SOS events.
- [x] Foreground service (`MarineMonitoringService`) running active background monitoring with high-priority notification channel.
