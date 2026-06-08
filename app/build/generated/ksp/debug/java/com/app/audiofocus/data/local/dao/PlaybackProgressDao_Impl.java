package com.app.audiofocus.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.app.audiofocus.data.local.entity.PlaybackProgressEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PlaybackProgressDao_Impl implements PlaybackProgressDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PlaybackProgressEntity> __insertionAdapterOfPlaybackProgressEntity;

  public PlaybackProgressDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPlaybackProgressEntity = new EntityInsertionAdapter<PlaybackProgressEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `playback_progress` (`audiobookId`,`positionMs`,`durationMs`,`progressPercent`,`status`,`lastPlayedAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlaybackProgressEntity entity) {
        statement.bindString(1, entity.getAudiobookId());
        statement.bindLong(2, entity.getPositionMs());
        statement.bindLong(3, entity.getDurationMs());
        statement.bindLong(4, entity.getProgressPercent());
        statement.bindString(5, entity.getStatus());
        if (entity.getLastPlayedAt() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getLastPlayedAt());
        }
        statement.bindString(7, entity.getUpdatedAt());
      }
    };
  }

  @Override
  public Object upsert(final PlaybackProgressEntity progress,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlaybackProgressEntity.insert(progress);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<PlaybackProgressEntity>> observeAll() {
    final String _sql = "SELECT * FROM playback_progress";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"playback_progress"}, new Callable<List<PlaybackProgressEntity>>() {
      @Override
      @NonNull
      public List<PlaybackProgressEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAudiobookId = CursorUtil.getColumnIndexOrThrow(_cursor, "audiobookId");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfProgressPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "progressPercent");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<PlaybackProgressEntity> _result = new ArrayList<PlaybackProgressEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlaybackProgressEntity _item;
            final String _tmpAudiobookId;
            _tmpAudiobookId = _cursor.getString(_cursorIndexOfAudiobookId);
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpProgressPercent;
            _tmpProgressPercent = _cursor.getInt(_cursorIndexOfProgressPercent);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpLastPlayedAt;
            if (_cursor.isNull(_cursorIndexOfLastPlayedAt)) {
              _tmpLastPlayedAt = null;
            } else {
              _tmpLastPlayedAt = _cursor.getString(_cursorIndexOfLastPlayedAt);
            }
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            _item = new PlaybackProgressEntity(_tmpAudiobookId,_tmpPositionMs,_tmpDurationMs,_tmpProgressPercent,_tmpStatus,_tmpLastPlayedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<PlaybackProgressEntity> observeByAudiobookId(final String audiobookId) {
    final String _sql = "SELECT * FROM playback_progress WHERE audiobookId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, audiobookId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"playback_progress"}, new Callable<PlaybackProgressEntity>() {
      @Override
      @Nullable
      public PlaybackProgressEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAudiobookId = CursorUtil.getColumnIndexOrThrow(_cursor, "audiobookId");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfProgressPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "progressPercent");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final PlaybackProgressEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpAudiobookId;
            _tmpAudiobookId = _cursor.getString(_cursorIndexOfAudiobookId);
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpProgressPercent;
            _tmpProgressPercent = _cursor.getInt(_cursorIndexOfProgressPercent);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpLastPlayedAt;
            if (_cursor.isNull(_cursorIndexOfLastPlayedAt)) {
              _tmpLastPlayedAt = null;
            } else {
              _tmpLastPlayedAt = _cursor.getString(_cursorIndexOfLastPlayedAt);
            }
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            _result = new PlaybackProgressEntity(_tmpAudiobookId,_tmpPositionMs,_tmpDurationMs,_tmpProgressPercent,_tmpStatus,_tmpLastPlayedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getByAudiobookId(final String audiobookId,
      final Continuation<? super PlaybackProgressEntity> $completion) {
    final String _sql = "SELECT * FROM playback_progress WHERE audiobookId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, audiobookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PlaybackProgressEntity>() {
      @Override
      @Nullable
      public PlaybackProgressEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfAudiobookId = CursorUtil.getColumnIndexOrThrow(_cursor, "audiobookId");
          final int _cursorIndexOfPositionMs = CursorUtil.getColumnIndexOrThrow(_cursor, "positionMs");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfProgressPercent = CursorUtil.getColumnIndexOrThrow(_cursor, "progressPercent");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfLastPlayedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastPlayedAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final PlaybackProgressEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpAudiobookId;
            _tmpAudiobookId = _cursor.getString(_cursorIndexOfAudiobookId);
            final long _tmpPositionMs;
            _tmpPositionMs = _cursor.getLong(_cursorIndexOfPositionMs);
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final int _tmpProgressPercent;
            _tmpProgressPercent = _cursor.getInt(_cursorIndexOfProgressPercent);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpLastPlayedAt;
            if (_cursor.isNull(_cursorIndexOfLastPlayedAt)) {
              _tmpLastPlayedAt = null;
            } else {
              _tmpLastPlayedAt = _cursor.getString(_cursorIndexOfLastPlayedAt);
            }
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            _result = new PlaybackProgressEntity(_tmpAudiobookId,_tmpPositionMs,_tmpDurationMs,_tmpProgressPercent,_tmpStatus,_tmpLastPlayedAt,_tmpUpdatedAt);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
