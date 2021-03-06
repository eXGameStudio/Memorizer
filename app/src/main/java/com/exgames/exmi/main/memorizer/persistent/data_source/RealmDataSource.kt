package com.exgames.exmi.main.memorizer.persistent.data_source

import android.support.annotation.NonNull
import android.util.Log
import com.exgames.exmi.main.memorizer.persistent.domain.HighScores
import io.realm.Realm
import io.realm.RealmObject
import io.realm.Sort
import io.realm.com_exgames_exmi_main_memorizer_persistent_realm_entities_RHighScoresRealmProxy


abstract class RealmDataSource<T : RealmObject>(private var clazz: Class<T>) {

    fun create(@NonNull realmInstance: Realm, @NonNull input: T) {
        realmInstance.executeTransaction { realm ->
            val start = System.currentTimeMillis()
            realm.copyToRealm(input)
            Log.d(clazz.simpleName, StringBuilder(clazz.simpleName).append("Insertion time :").append(System.currentTimeMillis() - start).toString())
        }
    }

    fun createOrUpdate(@NonNull realmInstance: Realm, @NonNull input: T) {
        realmInstance.executeTransaction { realm ->
            val start = System.currentTimeMillis()
            realm.copyToRealmOrUpdate(input)
            Log.d(clazz.simpleName, StringBuilder(clazz.simpleName).append("Insertion time :").append(System.currentTimeMillis() - start).toString())
        }
    }

    fun getFirst(@NonNull realmInstance: Realm, @NonNull input: T): T? = realmInstance.where(clazz).findFirst()

    fun getAll(@NonNull realmInstance: Realm): MutableList<T> {
        return realmInstance.where(clazz).findAll().toMutableList()
    }

    fun count(@NonNull realmInstance: Realm): Long {
        return realmInstance.where(clazz).count()
    }

    abstract fun getPrimaryKey(): String

    fun clearAll(@NonNull realmInstance: Realm) {
        realmInstance.executeTransaction { realm ->
            val start = System.currentTimeMillis()
            realm.deleteAll()
            Log.d(clazz.simpleName, StringBuilder(clazz.simpleName).append("Insertion time :").append(System.currentTimeMillis() - start).toString())
        }
    }

    fun getTopXElements(realm: Realm, field: String, elementsToGet: Int): MutableList<T> {
        val results = realm.where(clazz).sort(field, Sort.ASCENDING).findAll()
        return if (results.size >= elementsToGet) {
            results.subList(0, elementsToGet).toMutableList()
        } else {
            results.subList(0, results.size).toMutableList()
        }
    }

    fun clear(realmInstance: Realm, firstField: String, secondField: String, firstFieldValue: String, secondFieldValue: String) {
        realmInstance.executeTransaction { realm ->
            val results = realm.where(clazz)
                    .equalTo(firstField, firstFieldValue)
                    .and()
                    .equalTo(secondField, secondFieldValue)
                    .findFirst()
            results?.deleteFromRealm()
        }
    }
}