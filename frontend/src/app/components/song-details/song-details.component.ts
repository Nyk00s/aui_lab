import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Song } from '../../models/song.model';

@Component({
  selector: 'app-song-details',
  templateUrl: './song-details.component.html'
})
export class SongDetailsComponent implements OnInit {
  song: Song | null = null;
  albumId: string | null = null;
  songId: string | null = null;

  constructor(private api: ApiService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(pm => {
      this.albumId = pm.get('albumId');
      this.songId = pm.get('songId');

      if (this.songId) {
        this.api.getSong(this.songId).subscribe({
          next: x => this.song = x,
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
