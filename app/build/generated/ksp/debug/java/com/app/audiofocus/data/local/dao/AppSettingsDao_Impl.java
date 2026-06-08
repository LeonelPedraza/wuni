package com.app.audiofocus.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.app.audiofocus.data.local.entity.AppSettingsEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppSettingsDao_Impl implements AppSettingsDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AppSettingsEntity> __insertionAdapterOfAppSettingsEntity;

  public AppSettingsDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAppSettingsEntity = new EntityInsertionAdapter<AppSettingsEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `app_settings` (`id`,`skipSeconds`,`autoScanEnabled`,`theme`,`showPossibleAudiobooks`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AppSettingsEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindLong(2, entity.getSkipSeconds());
        final int _tmp = entity.getAutoScanEnabled() ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindString(4, entity.getTheme());
        final int _tmp_1 = entity.getShowPossibleAudiobooks() ? 1 : 0;
        statement.bindLong(5, _tmp_1);
        statement.bindString(6, entity.getCreatedAt());
        statement.bindString(7, entity.getUpdatedAt());
      }
    };
  }

  @Override
  public Object upsert(final AppSettingsEntity settings,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAppSettingsEntity.insert(settings);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM app_settings";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
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
