import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Album } from '../../models/album.model';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ChangeDetectorRef } from '@angular/core';

@Component({
  selector: 'app-album-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './album-list.component.html'
})
export class AlbumListComponent implements OnInit {
  album: Album[] = [];
  loading = false;
  error = '';

  constructor(private api: ApiService, private router: Router, private cd: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.load();
  }

  load() {
    this.loading = true;
    this.api.getAlbums().subscribe({
      next: data => { console.log('Albums Received: ', data); this.album = data; this.loading = false; this.cd.detectChanges(); },
      error: err => { console.error(); this.error = 'Error while getting album'; this.loading = false; this.cd.detectChanges(); }
    });
  }

  goAdd() {
    this.router.navigate(['/albums/add']);
  }

  edit(cat: Album) {
    this.router.navigate(['/albums/edit', cat.id]);
  }

  viewDetails(cat: Album) {
    this.router.navigate(['/albums', cat.id]);
  }

  remove(cat: Album) {
    if (!confirm(`Delete Album "${cat.name}" ?`)) return;
    this.api.deleteAlbum(cat.id!).subscribe({
      next: () => this.load(),
      error: () => alert('error while deleting')
    });
  }
}