import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Album } from '../models/album.model';
import { Song } from '../models/song.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private base = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getAlbums(): Observable<Album[]> {
    return this.http.get<Album[]>(`${this.base}/albums`);
  }
  getAlbum(id: string | number): Observable<Album> {
    return this.http.get<Album>(`${this.base}/albums/${id}`);
  }
  getAlbumSongs(id: string | number): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.base}/albums/${id}/songs`)
  }
  addAlbum(album: Album): Observable<Album> {
    return this.http.post<Album>(`${this.base}/albums`, album);
  }
  updateAlbum(id: string | number, album: Album): Observable<Album> {
    return this.http.put<Album>(`${this.base}/albums/${id}`, album);
  }
  deleteAlbum(id: string | number): Observable<void> {
    return this.http.delete<void>(`${this.base}/albums/${id}`);
  }

  


  getSongs(): Observable<Song[]> {
    return this.http.get<Song[]>(`${this.base}/songs`);
  }
  getSong(id: string | number): Observable<Song> {
    return this.http.get<Song>(`${this.base}/songs/${id}`);
  }
  addSong(song: Song): Observable<Song> {
    return this.http.post<Song>(`${this.base}/songs`, song);
  }
  updateSong(id: string | number, song: Song): Observable<Song> {
    return this.http.put<Song>(`${this.base}/songs/${id}`, song);
  }
  deleteSong(id: string | number): Observable<void> {
    return this.http.delete<void>(`${this.base}/songs/${id}`);
  }

}
