package com.app.audiofocus.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.app.audiofocus.data.local.entity.AudiobookEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
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
public final class AudiobookDao_Impl implements AudiobookDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<AudiobookEntity> __insertionAdapterOfAudiobookEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSampleRows;

  private final SharedSQLiteStatement __preparedStmtOfSetFavorite;

  private final SharedSQLiteStatement __preparedStmtOfSetVisibility;

  private final SharedSQLiteStatement __preparedStmtOfMarkDeleted;

  public AudiobookDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfAudiobookEntity = new EntityInsertionAdapter<AudiobookEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `audiobooks` (`id`,`mediaStoreId`,`uri`,`fingerprint`,`displayName`,`title`,`author`,`album`,`durationMs`,`sizeBytes`,`mimeType`,`relativePath`,`dateModified`,`coverUri`,`classification`,`discoveryScore`,`isFavorite`,`visibilityStatus`,`userDecision`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final AudiobookEntity entity) {
        statement.bindString(1, entity.getId());
        if (entity.getMediaStoreId() == null) {
          statement.bindNull(2);
        } else {
          statement.bindLong(2, entity.getMediaStoreId());
        }
        statement.bindString(3, entity.getUri());
        statement.bindString(4, entity.getFingerprint());
        statement.bindString(5, entity.getDisplayName());
        statement.bindString(6, entity.getTitle());
        if (entity.getAuthor() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getAuthor());
        }
        if (entity.getAlbum() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getAlbum());
        }
        statement.bindLong(9, entity.getDurationMs());
        if (entity.getSizeBytes() == null) {
          statement.bindNull(10);
        } else {
          statement.bindLong(10, entity.getSizeBytes());
        }
        if (entity.getMimeType() == null) {
          statement.bindNull(11);
        } else {
          statement.bindString(11, entity.getMimeType());
        }
        if (entity.getRelativePath() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getRelativePath());
        }
        if (entity.getDateModified() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getDateModified());
        }
        if (entity.getCoverUri() == null) {
          statement.bindNull(14);
        } else {
          statement.bindString(14, entity.getCoverUri());
        }
        statement.bindString(15, entity.getClassification());
        statement.bindLong(16, entity.getDiscoveryScore());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(17, _tmp);
        statement.bindString(18, entity.getVisibilityStatus());
        if (entity.getUserDecision() == null) {
          statement.bindNull(19);
        } else {
          statement.bindString(19, entity.getUserDecision());
        }
        statement.bindString(20, entity.getCreatedAt());
        statement.bindString(21, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM audiobooks WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteSampleRows = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM audiobooks WHERE uri LIKE 'content://sample/%'";
        return _query;
      }
    };
    this.__preparedStmtOfSetFavorite = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE audiobooks SET isFavorite = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSetVisibility = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE audiobooks SET visibilityStatus = ?, userDecision = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkDeleted = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE audiobooks SET visibilityStatus = 'deleted', userDecision = 'deleted', isFavorite = 0, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object upsertAll(final List<AudiobookEntity> items,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfAudiobookEntity.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final String id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSampleRows(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSampleRows.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSampleRows.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setFavorite(final String id, final boolean isFavorite, final String updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetFavorite.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetFavorite.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object setVisibility(final String id, final String visibilityStatus,
      final String userDecision, final String updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSetVisibility.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, visibilityStatus);
        _argIndex = 2;
        if (userDecision == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, userDecision);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, updatedAt);
        _argIndex = 4;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfSetVisibility.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markDeleted(final String id, final String updatedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkDeleted.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, updatedAt);
        _argIndex = 2;
        _stmt.bindString(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkDeleted.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<AudiobookEntity>> observeVisibleAudiobooks() {
    final String _sql = "SELECT * FROM audiobooks WHERE visibilityStatus = 'active' AND COALESCE(userDecision, '') != 'deleted' ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audiobooks"}, new Callable<List<AudiobookEntity>>() {
      @Override
      @NonNull
      public List<AudiobookEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaStoreId");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfFingerprint = CursorUtil.getColumnIndexOrThrow(_cursor, "fingerprint");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "album");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfRelativePath = CursorUtil.getColumnIndexOrThrow(_cursor, "relativePath");
          final int _cursorIndexOfDateModified = CursorUtil.getColumnIndexOrThrow(_cursor, "dateModified");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfClassification = CursorUtil.getColumnIndexOrThrow(_cursor, "classification");
          final int _cursorIndexOfDiscoveryScore = CursorUtil.getColumnIndexOrThrow(_cursor, "discoveryScore");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfVisibilityStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "visibilityStatus");
          final int _cursorIndexOfUserDecision = CursorUtil.getColumnIndexOrThrow(_cursor, "userDecision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AudiobookEntity> _result = new ArrayList<AudiobookEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AudiobookEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final Long _tmpMediaStoreId;
            if (_cursor.isNull(_cursorIndexOfMediaStoreId)) {
              _tmpMediaStoreId = null;
            } else {
              _tmpMediaStoreId = _cursor.getLong(_cursorIndexOfMediaStoreId);
            }
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpFingerprint;
            _tmpFingerprint = _cursor.getString(_cursorIndexOfFingerprint);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final String _tmpAlbum;
            if (_cursor.isNull(_cursorIndexOfAlbum)) {
              _tmpAlbum = null;
            } else {
              _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final Long _tmpSizeBytes;
            if (_cursor.isNull(_cursorIndexOfSizeBytes)) {
              _tmpSizeBytes = null;
            } else {
              _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final String _tmpRelativePath;
            if (_cursor.isNull(_cursorIndexOfRelativePath)) {
              _tmpRelativePath = null;
            } else {
              _tmpRelativePath = _cursor.getString(_cursorIndexOfRelativePath);
            }
            final Long _tmpDateModified;
            if (_cursor.isNull(_cursorIndexOfDateModified)) {
              _tmpDateModified = null;
            } else {
              _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
            }
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final String _tmpClassification;
            _tmpClassification = _cursor.getString(_cursorIndexOfClassification);
            final int _tmpDiscoveryScore;
            _tmpDiscoveryScore = _cursor.getInt(_cursorIndexOfDiscoveryScore);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final String _tmpVisibilityStatus;
            _tmpVisibilityStatus = _cursor.getString(_cursorIndexOfVisibilityStatus);
            final String _tmpUserDecision;
            if (_cursor.isNull(_cursorIndexOfUserDecision)) {
              _tmpUserDecision = null;
            } else {
              _tmpUserDecision = _cursor.getString(_cursorIndexOfUserDecision);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            _item = new AudiobookEntity(_tmpId,_tmpMediaStoreId,_tmpUri,_tmpFingerprint,_tmpDisplayName,_tmpTitle,_tmpAuthor,_tmpAlbum,_tmpDurationMs,_tmpSizeBytes,_tmpMimeType,_tmpRelativePath,_tmpDateModified,_tmpCoverUri,_tmpClassification,_tmpDiscoveryScore,_tmpIsFavorite,_tmpVisibilityStatus,_tmpUserDecision,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Flow<List<AudiobookEntity>> observeHiddenAudiobooks() {
    final String _sql = "SELECT * FROM audiobooks WHERE visibilityStatus = 'hidden' AND COALESCE(userDecision, '') != 'deleted' ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audiobooks"}, new Callable<List<AudiobookEntity>>() {
      @Override
      @NonNull
      public List<AudiobookEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaStoreId");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfFingerprint = CursorUtil.getColumnIndexOrThrow(_cursor, "fingerprint");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "album");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfRelativePath = CursorUtil.getColumnIndexOrThrow(_cursor, "relativePath");
          final int _cursorIndexOfDateModified = CursorUtil.getColumnIndexOrThrow(_cursor, "dateModified");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfClassification = CursorUtil.getColumnIndexOrThrow(_cursor, "classification");
          final int _cursorIndexOfDiscoveryScore = CursorUtil.getColumnIndexOrThrow(_cursor, "discoveryScore");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfVisibilityStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "visibilityStatus");
          final int _cursorIndexOfUserDecision = CursorUtil.getColumnIndexOrThrow(_cursor, "userDecision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AudiobookEntity> _result = new ArrayList<AudiobookEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AudiobookEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final Long _tmpMediaStoreId;
            if (_cursor.isNull(_cursorIndexOfMediaStoreId)) {
              _tmpMediaStoreId = null;
            } else {
              _tmpMediaStoreId = _cursor.getLong(_cursorIndexOfMediaStoreId);
            }
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpFingerprint;
            _tmpFingerprint = _cursor.getString(_cursorIndexOfFingerprint);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final String _tmpAlbum;
            if (_cursor.isNull(_cursorIndexOfAlbum)) {
              _tmpAlbum = null;
            } else {
              _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final Long _tmpSizeBytes;
            if (_cursor.isNull(_cursorIndexOfSizeBytes)) {
              _tmpSizeBytes = null;
            } else {
              _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final String _tmpRelativePath;
            if (_cursor.isNull(_cursorIndexOfRelativePath)) {
              _tmpRelativePath = null;
            } else {
              _tmpRelativePath = _cursor.getString(_cursorIndexOfRelativePath);
            }
            final Long _tmpDateModified;
            if (_cursor.isNull(_cursorIndexOfDateModified)) {
              _tmpDateModified = null;
            } else {
              _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
            }
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final String _tmpClassification;
            _tmpClassification = _cursor.getString(_cursorIndexOfClassification);
            final int _tmpDiscoveryScore;
            _tmpDiscoveryScore = _cursor.getInt(_cursorIndexOfDiscoveryScore);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final String _tmpVisibilityStatus;
            _tmpVisibilityStatus = _cursor.getString(_cursorIndexOfVisibilityStatus);
            final String _tmpUserDecision;
            if (_cursor.isNull(_cursorIndexOfUserDecision)) {
              _tmpUserDecision = null;
            } else {
              _tmpUserDecision = _cursor.getString(_cursorIndexOfUserDecision);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            _item = new AudiobookEntity(_tmpId,_tmpMediaStoreId,_tmpUri,_tmpFingerprint,_tmpDisplayName,_tmpTitle,_tmpAuthor,_tmpAlbum,_tmpDurationMs,_tmpSizeBytes,_tmpMimeType,_tmpRelativePath,_tmpDateModified,_tmpCoverUri,_tmpClassification,_tmpDiscoveryScore,_tmpIsFavorite,_tmpVisibilityStatus,_tmpUserDecision,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getLibraryAudiobooks(
      final Continuation<? super List<AudiobookEntity>> $completion) {
    final String _sql = "SELECT * FROM audiobooks WHERE COALESCE(userDecision, '') != 'deleted'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<AudiobookEntity>>() {
      @Override
      @NonNull
      public List<AudiobookEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaStoreId");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfFingerprint = CursorUtil.getColumnIndexOrThrow(_cursor, "fingerprint");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "album");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfRelativePath = CursorUtil.getColumnIndexOrThrow(_cursor, "relativePath");
          final int _cursorIndexOfDateModified = CursorUtil.getColumnIndexOrThrow(_cursor, "dateModified");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfClassification = CursorUtil.getColumnIndexOrThrow(_cursor, "classification");
          final int _cursorIndexOfDiscoveryScore = CursorUtil.getColumnIndexOrThrow(_cursor, "discoveryScore");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfVisibilityStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "visibilityStatus");
          final int _cursorIndexOfUserDecision = CursorUtil.getColumnIndexOrThrow(_cursor, "userDecision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<AudiobookEntity> _result = new ArrayList<AudiobookEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final AudiobookEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final Long _tmpMediaStoreId;
            if (_cursor.isNull(_cursorIndexOfMediaStoreId)) {
              _tmpMediaStoreId = null;
            } else {
              _tmpMediaStoreId = _cursor.getLong(_cursorIndexOfMediaStoreId);
            }
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpFingerprint;
            _tmpFingerprint = _cursor.getString(_cursorIndexOfFingerprint);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final String _tmpAlbum;
            if (_cursor.isNull(_cursorIndexOfAlbum)) {
              _tmpAlbum = null;
            } else {
              _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final Long _tmpSizeBytes;
            if (_cursor.isNull(_cursorIndexOfSizeBytes)) {
              _tmpSizeBytes = null;
            } else {
              _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final String _tmpRelativePath;
            if (_cursor.isNull(_cursorIndexOfRelativePath)) {
              _tmpRelativePath = null;
            } else {
              _tmpRelativePath = _cursor.getString(_cursorIndexOfRelativePath);
            }
            final Long _tmpDateModified;
            if (_cursor.isNull(_cursorIndexOfDateModified)) {
              _tmpDateModified = null;
            } else {
              _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
            }
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final String _tmpClassification;
            _tmpClassification = _cursor.getString(_cursorIndexOfClassification);
            final int _tmpDiscoveryScore;
            _tmpDiscoveryScore = _cursor.getInt(_cursorIndexOfDiscoveryScore);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final String _tmpVisibilityStatus;
            _tmpVisibilityStatus = _cursor.getString(_cursorIndexOfVisibilityStatus);
            final String _tmpUserDecision;
            if (_cursor.isNull(_cursorIndexOfUserDecision)) {
              _tmpUserDecision = null;
            } else {
              _tmpUserDecision = _cursor.getString(_cursorIndexOfUserDecision);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            _item = new AudiobookEntity(_tmpId,_tmpMediaStoreId,_tmpUri,_tmpFingerprint,_tmpDisplayName,_tmpTitle,_tmpAuthor,_tmpAlbum,_tmpDurationMs,_tmpSizeBytes,_tmpMimeType,_tmpRelativePath,_tmpDateModified,_tmpCoverUri,_tmpClassification,_tmpDiscoveryScore,_tmpIsFavorite,_tmpVisibilityStatus,_tmpUserDecision,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<AudiobookEntity> observeAudiobook(final String id) {
    final String _sql = "SELECT * FROM audiobooks WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audiobooks"}, new Callable<AudiobookEntity>() {
      @Override
      @Nullable
      public AudiobookEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaStoreId");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfFingerprint = CursorUtil.getColumnIndexOrThrow(_cursor, "fingerprint");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "album");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfRelativePath = CursorUtil.getColumnIndexOrThrow(_cursor, "relativePath");
          final int _cursorIndexOfDateModified = CursorUtil.getColumnIndexOrThrow(_cursor, "dateModified");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfClassification = CursorUtil.getColumnIndexOrThrow(_cursor, "classification");
          final int _cursorIndexOfDiscoveryScore = CursorUtil.getColumnIndexOrThrow(_cursor, "discoveryScore");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfVisibilityStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "visibilityStatus");
          final int _cursorIndexOfUserDecision = CursorUtil.getColumnIndexOrThrow(_cursor, "userDecision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final AudiobookEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final Long _tmpMediaStoreId;
            if (_cursor.isNull(_cursorIndexOfMediaStoreId)) {
              _tmpMediaStoreId = null;
            } else {
              _tmpMediaStoreId = _cursor.getLong(_cursorIndexOfMediaStoreId);
            }
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpFingerprint;
            _tmpFingerprint = _cursor.getString(_cursorIndexOfFingerprint);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final String _tmpAlbum;
            if (_cursor.isNull(_cursorIndexOfAlbum)) {
              _tmpAlbum = null;
            } else {
              _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final Long _tmpSizeBytes;
            if (_cursor.isNull(_cursorIndexOfSizeBytes)) {
              _tmpSizeBytes = null;
            } else {
              _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final String _tmpRelativePath;
            if (_cursor.isNull(_cursorIndexOfRelativePath)) {
              _tmpRelativePath = null;
            } else {
              _tmpRelativePath = _cursor.getString(_cursorIndexOfRelativePath);
            }
            final Long _tmpDateModified;
            if (_cursor.isNull(_cursorIndexOfDateModified)) {
              _tmpDateModified = null;
            } else {
              _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
            }
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final String _tmpClassification;
            _tmpClassification = _cursor.getString(_cursorIndexOfClassification);
            final int _tmpDiscoveryScore;
            _tmpDiscoveryScore = _cursor.getInt(_cursorIndexOfDiscoveryScore);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final String _tmpVisibilityStatus;
            _tmpVisibilityStatus = _cursor.getString(_cursorIndexOfVisibilityStatus);
            final String _tmpUserDecision;
            if (_cursor.isNull(_cursorIndexOfUserDecision)) {
              _tmpUserDecision = null;
            } else {
              _tmpUserDecision = _cursor.getString(_cursorIndexOfUserDecision);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            _result = new AudiobookEntity(_tmpId,_tmpMediaStoreId,_tmpUri,_tmpFingerprint,_tmpDisplayName,_tmpTitle,_tmpAuthor,_tmpAlbum,_tmpDurationMs,_tmpSizeBytes,_tmpMimeType,_tmpRelativePath,_tmpDateModified,_tmpCoverUri,_tmpClassification,_tmpDiscoveryScore,_tmpIsFavorite,_tmpVisibilityStatus,_tmpUserDecision,_tmpCreatedAt,_tmpUpdatedAt);
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
  public Object getAudiobook(final String id,
      final Continuation<? super AudiobookEntity> $completion) {
    final String _sql = "SELECT * FROM audiobooks WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<AudiobookEntity>() {
      @Override
      @Nullable
      public AudiobookEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfMediaStoreId = CursorUtil.getColumnIndexOrThrow(_cursor, "mediaStoreId");
          final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
          final int _cursorIndexOfFingerprint = CursorUtil.getColumnIndexOrThrow(_cursor, "fingerprint");
          final int _cursorIndexOfDisplayName = CursorUtil.getColumnIndexOrThrow(_cursor, "displayName");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfAlbum = CursorUtil.getColumnIndexOrThrow(_cursor, "album");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfSizeBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "sizeBytes");
          final int _cursorIndexOfMimeType = CursorUtil.getColumnIndexOrThrow(_cursor, "mimeType");
          final int _cursorIndexOfRelativePath = CursorUtil.getColumnIndexOrThrow(_cursor, "relativePath");
          final int _cursorIndexOfDateModified = CursorUtil.getColumnIndexOrThrow(_cursor, "dateModified");
          final int _cursorIndexOfCoverUri = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUri");
          final int _cursorIndexOfClassification = CursorUtil.getColumnIndexOrThrow(_cursor, "classification");
          final int _cursorIndexOfDiscoveryScore = CursorUtil.getColumnIndexOrThrow(_cursor, "discoveryScore");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfVisibilityStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "visibilityStatus");
          final int _cursorIndexOfUserDecision = CursorUtil.getColumnIndexOrThrow(_cursor, "userDecision");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final AudiobookEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final Long _tmpMediaStoreId;
            if (_cursor.isNull(_cursorIndexOfMediaStoreId)) {
              _tmpMediaStoreId = null;
            } else {
              _tmpMediaStoreId = _cursor.getLong(_cursorIndexOfMediaStoreId);
            }
            final String _tmpUri;
            _tmpUri = _cursor.getString(_cursorIndexOfUri);
            final String _tmpFingerprint;
            _tmpFingerprint = _cursor.getString(_cursorIndexOfFingerprint);
            final String _tmpDisplayName;
            _tmpDisplayName = _cursor.getString(_cursorIndexOfDisplayName);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpAuthor;
            if (_cursor.isNull(_cursorIndexOfAuthor)) {
              _tmpAuthor = null;
            } else {
              _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            }
            final String _tmpAlbum;
            if (_cursor.isNull(_cursorIndexOfAlbum)) {
              _tmpAlbum = null;
            } else {
              _tmpAlbum = _cursor.getString(_cursorIndexOfAlbum);
            }
            final long _tmpDurationMs;
            _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            final Long _tmpSizeBytes;
            if (_cursor.isNull(_cursorIndexOfSizeBytes)) {
              _tmpSizeBytes = null;
            } else {
              _tmpSizeBytes = _cursor.getLong(_cursorIndexOfSizeBytes);
            }
            final String _tmpMimeType;
            if (_cursor.isNull(_cursorIndexOfMimeType)) {
              _tmpMimeType = null;
            } else {
              _tmpMimeType = _cursor.getString(_cursorIndexOfMimeType);
            }
            final String _tmpRelativePath;
            if (_cursor.isNull(_cursorIndexOfRelativePath)) {
              _tmpRelativePath = null;
            } else {
              _tmpRelativePath = _cursor.getString(_cursorIndexOfRelativePath);
            }
            final Long _tmpDateModified;
            if (_cursor.isNull(_cursorIndexOfDateModified)) {
              _tmpDateModified = null;
            } else {
              _tmpDateModified = _cursor.getLong(_cursorIndexOfDateModified);
            }
            final String _tmpCoverUri;
            if (_cursor.isNull(_cursorIndexOfCoverUri)) {
              _tmpCoverUri = null;
            } else {
              _tmpCoverUri = _cursor.getString(_cursorIndexOfCoverUri);
            }
            final String _tmpClassification;
            _tmpClassification = _cursor.getString(_cursorIndexOfClassification);
            final int _tmpDiscoveryScore;
            _tmpDiscoveryScore = _cursor.getInt(_cursorIndexOfDiscoveryScore);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final String _tmpVisibilityStatus;
            _tmpVisibilityStatus = _cursor.getString(_cursorIndexOfVisibilityStatus);
            final String _tmpUserDecision;
            if (_cursor.isNull(_cursorIndexOfUserDecision)) {
              _tmpUserDecision = null;
            } else {
              _tmpUserDecision = _cursor.getString(_cursorIndexOfUserDecision);
            }
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final String _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getString(_cursorIndexOfUpdatedAt);
            _result = new AudiobookEntity(_tmpId,_tmpMediaStoreId,_tmpUri,_tmpFingerprint,_tmpDisplayName,_tmpTitle,_tmpAuthor,_tmpAlbum,_tmpDurationMs,_tmpSizeBytes,_tmpMimeType,_tmpRelativePath,_tmpDateModified,_tmpCoverUri,_tmpClassification,_tmpDiscoveryScore,_tmpIsFavorite,_tmpVisibilityStatus,_tmpUserDecision,_tmpCreatedAt,_tmpUpdatedAt);
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

  @Override
  public Flow<Integer> observeHiddenCount() {
    final String _sql = "SELECT COUNT(*) FROM audiobooks WHERE visibilityStatus = 'hidden'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"audiobooks"}, new Callable<Integer>() {
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
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object count(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM audiobooks";
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
