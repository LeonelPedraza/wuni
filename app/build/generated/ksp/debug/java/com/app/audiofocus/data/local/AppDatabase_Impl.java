package com.app.audiofocus.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.app.audiofocus.data.local.dao.AppSettingsDao;
import com.app.audiofocus.data.local.dao.AppSettingsDao_Impl;
import com.app.audiofocus.data.local.dao.AudiobookDao;
import com.app.audiofocus.data.local.dao.AudiobookDao_Impl;
import com.app.audiofocus.data.local.dao.PlaybackProgressDao;
import com.app.audiofocus.data.local.dao.PlaybackProgressDao_Impl;
import com.app.audiofocus.data.local.dao.ScanStateDao;
import com.app.audiofocus.data.local.dao.ScanStateDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile AudiobookDao _audiobookDao;

  private volatile PlaybackProgressDao _playbackProgressDao;

  private volatile ScanStateDao _scanStateDao;

  private volatile AppSettingsDao _appSettingsDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `audiobooks` (`id` TEXT NOT NULL, `mediaStoreId` INTEGER, `uri` TEXT NOT NULL, `fingerprint` TEXT NOT NULL, `displayName` TEXT NOT NULL, `title` TEXT NOT NULL, `author` TEXT, `album` TEXT, `durationMs` INTEGER NOT NULL, `sizeBytes` INTEGER, `mimeType` TEXT, `relativePath` TEXT, `dateModified` INTEGER, `coverUri` TEXT, `classification` TEXT NOT NULL, `discoveryScore` INTEGER NOT NULL, `isFavorite` INTEGER NOT NULL, `visibilityStatus` TEXT NOT NULL, `userDecision` TEXT, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `playback_progress` (`audiobookId` TEXT NOT NULL, `positionMs` INTEGER NOT NULL, `durationMs` INTEGER NOT NULL, `progressPercent` INTEGER NOT NULL, `status` TEXT NOT NULL, `lastPlayedAt` TEXT, `updatedAt` TEXT NOT NULL, PRIMARY KEY(`audiobookId`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `scan_state` (`id` TEXT NOT NULL, `lastScanAt` TEXT, `lastBackgroundSyncAt` TEXT, `lastMediaStoreVersion` TEXT, `totalFound` INTEGER NOT NULL, `totalAdded` INTEGER NOT NULL, `totalUpdated` INTEGER NOT NULL, `totalHidden` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `app_settings` (`id` TEXT NOT NULL, `skipSeconds` INTEGER NOT NULL, `autoScanEnabled` INTEGER NOT NULL, `theme` TEXT NOT NULL, `showPossibleAudiobooks` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '39133fad5a23cbf9d8c84fce42e5ecb7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `audiobooks`");
        db.execSQL("DROP TABLE IF EXISTS `playback_progress`");
        db.execSQL("DROP TABLE IF EXISTS `scan_state`");
        db.execSQL("DROP TABLE IF EXISTS `app_settings`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsAudiobooks = new HashMap<String, TableInfo.Column>(21);
        _columnsAudiobooks.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("mediaStoreId", new TableInfo.Column("mediaStoreId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("uri", new TableInfo.Column("uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("fingerprint", new TableInfo.Column("fingerprint", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("displayName", new TableInfo.Column("displayName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("author", new TableInfo.Column("author", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("album", new TableInfo.Column("album", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("durationMs", new TableInfo.Column("durationMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("sizeBytes", new TableInfo.Column("sizeBytes", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("mimeType", new TableInfo.Column("mimeType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("relativePath", new TableInfo.Column("relativePath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("dateModified", new TableInfo.Column("dateModified", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("coverUri", new TableInfo.Column("coverUri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("classification", new TableInfo.Column("classification", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("discoveryScore", new TableInfo.Column("discoveryScore", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("visibilityStatus", new TableInfo.Column("visibilityStatus", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("userDecision", new TableInfo.Column("userDecision", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAudiobooks.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAudiobooks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAudiobooks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAudiobooks = new TableInfo("audiobooks", _columnsAudiobooks, _foreignKeysAudiobooks, _indicesAudiobooks);
        final TableInfo _existingAudiobooks = TableInfo.read(db, "audiobooks");
        if (!_infoAudiobooks.equals(_existingAudiobooks)) {
          return new RoomOpenHelper.ValidationResult(false, "audiobooks(com.app.audiofocus.data.local.entity.AudiobookEntity).\n"
                  + " Expected:\n" + _infoAudiobooks + "\n"
                  + " Found:\n" + _existingAudiobooks);
        }
        final HashMap<String, TableInfo.Column> _columnsPlaybackProgress = new HashMap<String, TableInfo.Column>(7);
        _columnsPlaybackProgress.put("audiobookId", new TableInfo.Column("audiobookId", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackProgress.put("positionMs", new TableInfo.Column("positionMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackProgress.put("durationMs", new TableInfo.Column("durationMs", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackProgress.put("progressPercent", new TableInfo.Column("progressPercent", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackProgress.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackProgress.put("lastPlayedAt", new TableInfo.Column("lastPlayedAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlaybackProgress.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlaybackProgress = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPlaybackProgress = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPlaybackProgress = new TableInfo("playback_progress", _columnsPlaybackProgress, _foreignKeysPlaybackProgress, _indicesPlaybackProgress);
        final TableInfo _existingPlaybackProgress = TableInfo.read(db, "playback_progress");
        if (!_infoPlaybackProgress.equals(_existingPlaybackProgress)) {
          return new RoomOpenHelper.ValidationResult(false, "playback_progress(com.app.audiofocus.data.local.entity.PlaybackProgressEntity).\n"
                  + " Expected:\n" + _infoPlaybackProgress + "\n"
                  + " Found:\n" + _existingPlaybackProgress);
        }
        final HashMap<String, TableInfo.Column> _columnsScanState = new HashMap<String, TableInfo.Column>(8);
        _columnsScanState.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanState.put("lastScanAt", new TableInfo.Column("lastScanAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanState.put("lastBackgroundSyncAt", new TableInfo.Column("lastBackgroundSyncAt", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanState.put("lastMediaStoreVersion", new TableInfo.Column("lastMediaStoreVersion", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanState.put("totalFound", new TableInfo.Column("totalFound", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanState.put("totalAdded", new TableInfo.Column("totalAdded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanState.put("totalUpdated", new TableInfo.Column("totalUpdated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsScanState.put("totalHidden", new TableInfo.Column("totalHidden", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysScanState = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesScanState = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoScanState = new TableInfo("scan_state", _columnsScanState, _foreignKeysScanState, _indicesScanState);
        final TableInfo _existingScanState = TableInfo.read(db, "scan_state");
        if (!_infoScanState.equals(_existingScanState)) {
          return new RoomOpenHelper.ValidationResult(false, "scan_state(com.app.audiofocus.data.local.entity.ScanStateEntity).\n"
                  + " Expected:\n" + _infoScanState + "\n"
                  + " Found:\n" + _existingScanState);
        }
        final HashMap<String, TableInfo.Column> _columnsAppSettings = new HashMap<String, TableInfo.Column>(7);
        _columnsAppSettings.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("skipSeconds", new TableInfo.Column("skipSeconds", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("autoScanEnabled", new TableInfo.Column("autoScanEnabled", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("theme", new TableInfo.Column("theme", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("showPossibleAudiobooks", new TableInfo.Column("showPossibleAudiobooks", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsAppSettings.put("updatedAt", new TableInfo.Column("updatedAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysAppSettings = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesAppSettings = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoAppSettings = new TableInfo("app_settings", _columnsAppSettings, _foreignKeysAppSettings, _indicesAppSettings);
        final TableInfo _existingAppSettings = TableInfo.read(db, "app_settings");
        if (!_infoAppSettings.equals(_existingAppSettings)) {
          return new RoomOpenHelper.ValidationResult(false, "app_settings(com.app.audiofocus.data.local.entity.AppSettingsEntity).\n"
                  + " Expected:\n" + _infoAppSettings + "\n"
                  + " Found:\n" + _existingAppSettings);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "39133fad5a23cbf9d8c84fce42e5ecb7", "9f1edba2d32d15a855dfddf8c1d0dc87");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "audiobooks","playback_progress","scan_state","app_settings");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `audiobooks`");
      _db.execSQL("DELETE FROM `playback_progress`");
      _db.execSQL("DELETE FROM `scan_state`");
      _db.execSQL("DELETE FROM `app_settings`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(AudiobookDao.class, AudiobookDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PlaybackProgressDao.class, PlaybackProgressDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ScanStateDao.class, ScanStateDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(AppSettingsDao.class, AppSettingsDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public AudiobookDao audiobookDao() {
    if (_audiobookDao != null) {
      return _audiobookDao;
    } else {
      synchronized(this) {
        if(_audiobookDao == null) {
          _audiobookDao = new AudiobookDao_Impl(this);
        }
        return _audiobookDao;
      }
    }
  }

  @Override
  public PlaybackProgressDao playbackProgressDao() {
    if (_playbackProgressDao != null) {
      return _playbackProgressDao;
    } else {
      synchronized(this) {
        if(_playbackProgressDao == null) {
          _playbackProgressDao = new PlaybackProgressDao_Impl(this);
        }
        return _playbackProgressDao;
      }
    }
  }

  @Override
  public ScanStateDao scanStateDao() {
    if (_scanStateDao != null) {
      return _scanStateDao;
    } else {
      synchronized(this) {
        if(_scanStateDao == null) {
          _scanStateDao = new ScanStateDao_Impl(this);
        }
        return _scanStateDao;
      }
    }
  }

  @Override
  public AppSettingsDao appSettingsDao() {
    if (_appSettingsDao != null) {
      return _appSettingsDao;
    } else {
      synchronized(this) {
        if(_appSettingsDao == null) {
          _appSettingsDao = new AppSettingsDao_Impl(this);
        }
        return _appSettingsDao;
      }
    }
  }
}
