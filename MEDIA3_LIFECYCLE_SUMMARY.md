# Media3 lifecycle summary

`MediaSessionService` owns the single `ExoPlayer` instance, the active `MediaSession`, and the system-facing playback notification. Compose never owns the player directly.

The UI layer connects through a `MediaController` built from a `SessionToken`. ViewModels expose controller state as `StateFlow`, and screens collect it with `collectAsStateWithLifecycle()`. User actions like play, pause, seek, forward 10s, and rewind 10s go from Compose to ViewModel to `MediaController`.

The isolated prototype in `prototype/media/` proves the service/controller boundary and lock-screen notification path without touching Room, progress persistence, or the production `player/` package. After review, the production integration should add player listeners, map the active audiobook ID into StateFlow, and only then persist progress every 5 seconds and on the required lifecycle events.
