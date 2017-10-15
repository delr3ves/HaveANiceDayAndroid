package com.emabinalabs.haveaniceday.core.dao.database

import android.arch.persistence.db.framework.FrameworkSQLiteOpenHelperFactory
import android.arch.persistence.room.testing.MigrationTestHelper
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.emaginalabs.haveaniceday.core.dao.database.DBMigrations
import com.emaginalabs.haveaniceday.core.dao.database.HaveANiceDayDatabase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class Version1To2MigrationTest {

    val TEST_DB = "migration-test"

    @Rule
    lateinit var helper: MigrationTestHelper

    @Before
    fun setUp() {
        helper = MigrationTestHelper(
                InstrumentationRegistry.getInstrumentation(),
                HaveANiceDayDatabase::class.java.canonicalName,
                FrameworkSQLiteOpenHelperFactory())
    }

    @Test
    fun testMigrationV1ToV2() {
        helper.createDatabase(TEST_DB, 1).close()
        helper.runMigrationsAndValidate(
                TEST_DB,
                2,
                true,
                DBMigrations.MIGRATION_1_2)
    }
}
