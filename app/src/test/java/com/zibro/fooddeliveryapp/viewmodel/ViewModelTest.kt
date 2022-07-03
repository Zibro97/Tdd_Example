package com.zibro.fooddeliveryapp.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.zibro.fooddeliveryapp.di.appTestModule
import com.zibro.fooddeliveryapp.livedata.LiveDataTestObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

/**
 * Rule : 테스트 케이스를 실행하기 전후에 추가 코드를 실행할 수 있도록 도와줌
 * JUnit의 Annotation 호출 순서
 * > BeforeClass : 테스트 클래스 테스트 시작 시 1번만 호출
 * > Before : 테스트 케이스 시작전 각각 호출
 * > After : 테스트 케이스 완료시 각각 호출
 * > AfterClass : 테스트 클래스 모든 테스트 완료 시 1번 호출
 * */
@ExperimentalCoroutinesApi
open class ViewModelTest: KoinTest {
    //Mockito Rule 인스턴스 생성
    @get:Rule
    val mockitoRule : MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var context : Application

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        startKoin {
            androidContext(context)
            modules(appTestModule)
        }
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain() // MainDispatcher를 초기화 해주어야 메모리 누수가 발생하지 않음
    }

    protected fun <T> LiveData<T>.test(): LiveDataTestObserver<T> {
        val testObserver = LiveDataTestObserver<T>()
        observeForever(testObserver)

        return testObserver
    }

}