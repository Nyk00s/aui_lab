import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Song } from '../../models/song.model';

@Component({
  selector: 'app-song-details',
  templateUrl: './song-details.component.html'
})
export class SongDetailsComponent implements OnInit {
  el?: Song;
  albumId?: string;
  songId?: string;

  constructor(private api: ApiService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(pm => {
      this.albumId = pm.get('albumId') || pm.get('id');
      this.songId = pm.get('songId') || pm.get('id');
      const album = pm.get('albumId') || pm.get('id');
      const song = pm.get('songId') || (pm.get('songId') ? pm.get('songId') : null);
      this.albumId = pm.get('albumId') || pm.get('id');
      this.songId = pm.get('songId');

      if (this.albumId && this.songId) {
        this.api.getSong(this.songId).subscribe({
          next: x => this.el = x,
          error: () => alert('Error while fetching song')
        });
      }
    });
  }

  back() {
    if (this.albumId) this.router.navigate(['/albums', this.albumId]);
    else this.router.navigate(['/albums']);
  }

  edit() {
    if (this.albumId && this.songId) this.router.navigate(['/albums', this.albumId, 'songs', 'edit', this.songId]);
  }
}
