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
import com.app.audiofocus.data.local.entity.ScanStateEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class ScanStateDao_Impl implements ScanStateDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ScanStateEntity> __insertionAdapterOfScanStateEntity;

  public ScanStateDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfScanStateEntity = new EntityInsertionAdapter<ScanStateEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `scan_state` (`id`,`lastScanAt`,`lastBackgroundSyncAt`,`lastMediaStoreVersion`,`totalFound`,`totalAdded`,`totalUpdated`,`totalHidden`) VALUES (?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ScanStateEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getLastScanAt() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getLastScanAt());
        }
        if (entity.getLastBackgroundSyncAt() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getLastBackgroundSyncAt());
        }
        if (entity.getLastMediaStoreVersion() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getLastMediaStoreVersion());
        }
        statement.bindLong(5, entity.getTotalFound());
        statement.bindLong(6, entity.getTotalAdded());
        statement.bindLong(7, entity.getTotalUpdated());
        statement.bindLong(8, entity.getTotalHidden());
      }
    };
  }

  @Override
  public Object upsert(final ScanStateEntity state, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfScanStateEntity.insert(state);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<ScanStateEntity> observeById(final String id) {
    final String _sql = "SELECT * FROM scan_state WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"scan_state"}, new Callable<ScanStateEntity>() {
      @Override
      @Nullable
      public ScanStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLastScanAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastScanAt");
          final int _cursorIndexOfLastBackgroundSyncAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBackgroundSyncAt");
          final int _cursorIndexOfLastMediaStoreVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMediaStoreVersion");
          final int _cursorIndexOfTotalFound = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFound");
          final int _cursorIndexOfTotalAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAdded");
          final int _cursorIndexOfTotalUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUpdated");
          final int _cursorIndexOfTotalHidden = CursorUtil.getColumnIndexOrThrow(_cursor, "totalHidden");
          final ScanStateEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpLastScanAt;
            if (_cursor.isNull(_cursorIndexOfLastScanAt)) {
              _tmpLastScanAt = null;
            } else {
              _tmpLastScanAt = _cursor.getString(_cursorIndexOfLastScanAt);
            }
            final String _tmpLastBackgroundSyncAt;
            if (_cursor.isNull(_cursorIndexOfLastBackgroundSyncAt)) {
              _tmpLastBackgroundSyncAt = null;
            } else {
              _tmpLastBackgroundSyncAt = _cursor.getString(_cursorIndexOfLastBackgroundSyncAt);
            }
            final String _tmpLastMediaStoreVersion;
            if (_cursor.isNull(_cursorIndexOfLastMediaStoreVersion)) {
              _tmpLastMediaStoreVersion = null;
            } else {
              _tmpLastMediaStoreVersion = _cursor.getString(_cursorIndexOfLastMediaStoreVersion);
            }
            final int _tmpTotalFound;
            _tmpTotalFound = _cursor.getInt(_cursorIndexOfTotalFound);
            final int _tmpTotalAdded;
            _tmpTotalAdded = _cursor.getInt(_cursorIndexOfTotalAdded);
            final int _tmpTotalUpdated;
            _tmpTotalUpdated = _cursor.getInt(_cursorIndexOfTotalUpdated);
            final int _tmpTotalHidden;
            _tmpTotalHidden = _cursor.getInt(_cursorIndexOfTotalHidden);
            _result = new ScanStateEntity(_tmpId,_tmpLastScanAt,_tmpLastBackgroundSyncAt,_tmpLastMediaStoreVersion,_tmpTotalFound,_tmpTotalAdded,_tmpTotalUpdated,_tmpTotalHidden);
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
  public Object getById(final String id, final Continuation<? super ScanStateEntity> $completion) {
    final String _sql = "SELECT * FROM scan_state WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<ScanStateEntity>() {
      @Override
      @Nullable
      public ScanStateEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfLastScanAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastScanAt");
          final int _cursorIndexOfLastBackgroundSyncAt = CursorUtil.getColumnIndexOrThrow(_cursor, "lastBackgroundSyncAt");
          final int _cursorIndexOfLastMediaStoreVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "lastMediaStoreVersion");
          final int _cursorIndexOfTotalFound = CursorUtil.getColumnIndexOrThrow(_cursor, "totalFound");
          final int _cursorIndexOfTotalAdded = CursorUtil.getColumnIndexOrThrow(_cursor, "totalAdded");
          final int _cursorIndexOfTotalUpdated = CursorUtil.getColumnIndexOrThrow(_cursor, "totalUpdated");
          final int _cursorIndexOfTotalHidden = CursorUtil.getColumnIndexOrThrow(_cursor, "totalHidden");
          final ScanStateEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpLastScanAt;
            if (_cursor.isNull(_cursorIndexOfLastScanAt)) {
              _tmpLastScanAt = null;
            } else {
              _tmpLastScanAt = _cursor.getString(_cursorIndexOfLastScanAt);
            }
            final String _tmpLastBackgroundSyncAt;
            if (_cursor.isNull(_cursorIndexOfLastBackgroundSyncAt)) {
              _tmpLastBackgroundSyncAt = null;
            } else {
              _tmpLastBackgroundSyncAt = _cursor.getString(_cursorIndexOfLastBackgroundSyncAt);
            }
            final String _tmpLastMediaStoreVersion;
            if (_cursor.isNull(_cursorIndexOfLastMediaStoreVersion)) {
              _tmpLastMediaStoreVersion = null;
            } else {
              _tmpLastMediaStoreVersion = _cursor.getString(_cursorIndexOfLastMediaStoreVersion);
            }
            final int _tmpTotalFound;
            _tmpTotalFound = _cursor.getInt(_cursorIndexOfTotalFound);
            final int _tmpTotalAdded;
            _tmpTotalAdded = _cursor.getInt(_cursorIndexOfTotalAdded);
            final int _tmpTotalUpdated;
            _tmpTotalUpdated = _cursor.getInt(_cursorIndexOfTotalUpdated);
            final int _tmpTotalHidden;
            _tmpTotalHidden = _cursor.getInt(_cursorIndexOfTotalHidden);
            _result = new ScanStateEntity(_tmpId,_tmpLastScanAt,_tmpLastBackgroundSyncAt,_tmpLastMediaStoreVersion,_tmpTotalFound,_tmpTotalAdded,_tmpTotalUpdated,_tmpTotalHidden);
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
