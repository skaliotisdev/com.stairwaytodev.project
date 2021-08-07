package com.stairwaytodev.project.data.todo

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.stairwaytodev.project.data.*
import java.text.DateFormat

@Entity(tableName = TABLE_TODO)

class ToDoModel : Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    @ColumnInfo(name = TABLE_TODO_DONE)
    var isDone: Boolean
    var todo: String? = null

    @ColumnInfo(name = TABLE_TODO_TIME_SET)
    var deadlineTime: String? = null

    @ColumnInfo(name = TABLE_TODO_DATE_SET)
    var deadlineDate: String? = null

    @ColumnInfo(name = TABLE_TODO_DEADLINE)
    var deadlineTimeStamp: String?

    @ColumnInfo(name = TABLE_TODO_CREATION)
    var creationTimeStamp: Long? = null

    fun setDone(done: Int) {
        isDone = done == 1
    }

    val creationTimeStampFormatted: String get() = DateFormat.getDateTimeInstance().format(creationTimeStamp)

    constructor() {
        isDone = false
        deadlineTimeStamp = ""
        creationTimeStamp = System.currentTimeMillis()
    }

    constructor(`in`: Parcel) {
        id = `in`.readLong()
        isDone = `in`.readByte().toInt() != 0
        todo = `in`.readString()
        deadlineTime = `in`.readString()
        deadlineDate = `in`.readString()
        deadlineTimeStamp = `in`.readString()
        creationTimeStamp = if (`in`.readByte().toInt() == 0) {
            null
        } else {
            `in`.readLong()
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeByte((if (isDone) 1 else 0).toByte())
        dest.writeString(todo)
        dest.writeString(deadlineTime)
        dest.writeString(deadlineDate)
        dest.writeString(deadlineTimeStamp)
        if (creationTimeStamp == null) {
            dest.writeByte(0.toByte())
        } else {
            dest.writeByte(1.toByte())
            dest.writeLong(creationTimeStamp!!)
        }
    }

    companion object CREATOR : Creator<ToDoModel> {
        override fun createFromParcel(parcel: Parcel): ToDoModel {
            return ToDoModel(parcel)
        }

        override fun newArray(size: Int): Array<ToDoModel?> {
            return arrayOfNulls(size)
        }
    }
}