package com.ellison.jetpackdemo.dagger2.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ellison.jetpackdemo.room.MovieViewModel
import dagger.Module
import dagger.Subcomponent
import javax.inject.Inject

class MovieActivity : AppCompatActivity() {
    // 将子组件声明到对应的生命周期的Activity中
    lateinit var movieGraph: MovieGraph

    // 声明需要Dagger注入的ViewModel实例
    @Inject
    lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
//        // 创建子组件
//        movieGraph = (applicationContext as MyApplication).appGraph.movieGraph().create()
//        // 将ViewModel注入Activity
//        movieGraph.inject(this)

        super.onCreate(savedInstanceState)
    }
}

// 添加子组件
@Subcomponent
interface MovieGraph {
    @Subcomponent.Factory
    interface Factory {
        fun create(): MovieGraph
    }

    fun inject(movieActivity: MovieActivity)
}

// 添加子模块
@Module(subcomponents = [MovieGraph::class])
class MovieGraphModule {}