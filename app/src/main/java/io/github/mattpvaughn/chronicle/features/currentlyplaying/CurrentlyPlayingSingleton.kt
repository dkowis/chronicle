package io.github.mattpvaughn.chronicle.features.currentlyplaying

import android.support.v4.media.session.PlaybackStateCompat
import io.github.mattpvaughn.chronicle.data.model.*
import io.github.mattpvaughn.chronicle.features.player.EMPTY_PLAYBACK_STATE
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Singleton

/**
 * A global store of state containing information on the [Audiobook]/[MediaItemTrack]/[Chapter]
 * currently playing and the relevant playback information.
 */
@ExperimentalCoroutinesApi
interface CurrentlyPlaying {
    val book: StateFlow<Audiobook>
    val track: StateFlow<MediaItemTrack>
    val chapter: StateFlow<Chapter>
    val playbackState: PlaybackStateCompat

    fun setOnChapterChangeListener(listener: OnChapterChangeListener)
    fun update(track: MediaItemTrack, book: Audiobook, tracks: List<MediaItemTrack>)
}

interface OnChapterChangeListener {
    fun onChapterChange(chapter: Chapter)
}

/**
 * Implementation of [CurrentlyPlaying]. Values default to placeholder values until data is
 * made available (the user
 */
@ExperimentalCoroutinesApi
@Singleton
class CurrentlyPlayingSingleton : CurrentlyPlaying {
    override var book = MutableStateFlow(EMPTY_AUDIOBOOK)
    override var track = MutableStateFlow(EMPTY_TRACK)
    override var chapter = MutableStateFlow(EMPTY_CHAPTER)
    override var playbackState: PlaybackStateCompat = EMPTY_PLAYBACK_STATE

    private var tracks: MutableList<MediaItemTrack> = mutableListOf()
    private val chapters: MutableList<Chapter> = mutableListOf()

    private var listener: OnChapterChangeListener? = null

    override fun setOnChapterChangeListener(listener: OnChapterChangeListener) {
        this.listener = listener
    }

    override fun update(track: MediaItemTrack, book: Audiobook, tracks: List<MediaItemTrack>) {
        this.book.value = book
        this.track.value = track

        this.tracks.clear()
        this.tracks.addAll(tracks)

        this.chapters.addAll(
            if (book.chapters.isNotEmpty()) {
                book.chapters
            } else {
                tracks.asChapterList()
            }
        )

        if (tracks.isNotEmpty() && chapters.isNotEmpty()) {
            val chapter = chapters.getChapterAt(track.id.toLong(), track.progress)
            if (this.chapter.value != chapter) {
                this.chapter.value = chapter
                listener?.onChapterChange(chapter)
            }
        }

        printDebug()
    }

    private fun printDebug() {
        Timber.i("Currently Playing: track=${track.value.title}, index=${track.value.index}/${tracks.size}")
    }
}