package com.learning.moviesbrowser.viewmodel

import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.learning.moviesbrowser.utils.State
import com.learning.moviesbrowser.model.MovieListItem
import com.learning.moviesbrowser.repository.MoviesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val moviesRepository: MoviesRepository): ViewModel() {

    private val _movieLiveData = MutableLiveData<State<PagedList< MovieListItem>>>()
    val movieLiveData: LiveData<State<PagedList<MovieListItem>>>
        get() = _movieLiveData

    fun fetchMovieList(){
        viewModelScope.launch{
            moviesRepository.getAllMoviesList().collect{ state ->
                _movieLiveData.postValue(state)
            }
        }
    }

    fun fetchSearchMoviesList(query: String){
        viewModelScope.launch{
            moviesRepository.getSearchMoviesList(query = query).collect { state ->
                _movieLiveData.postValue(state)

            }
        }
    }

    fun fetchGenreNames(genreIds: List<String>) = moviesRepository.getGenreNames(genreIds).asLiveData()

    fun fetchMovieDetailsById(id: String, isSearch: Boolean = false) = moviesRepository.getMovieDetailById(id).asLiveData()

    fun fetchSearchMovieDetailById(id: String) = moviesRepository.getSearchMovieDetailById(id).asLiveData()

    fun markFavourite(movieId: String) = viewModelScope.launch{
            moviesRepository.markFavourite(movieId )
    }

    fun addMovieReview(movieId: String, reviewComment: String) = viewModelScope.launch{
        moviesRepository.addReviewToMovie(movieId, reviewComment)
    }

    fun clearSearch() = viewModelScope.launch { moviesRepository.clearSearchMovies() }


}