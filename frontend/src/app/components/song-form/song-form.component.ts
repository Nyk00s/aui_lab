import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Song } from '../../models/song.model';

@Component({
  selector: 'app-song-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './song-form.component.html'
})
export class SongFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  albumId?: string;
  songId?: string;

  constructor(
    private fb: FormBuilder,
    private api: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      seconds: ['', Validators.required],
      albumName: ['', Validators.required]
    });

    this.route.paramMap.subscribe(pm => {
      this.albumId = pm.get('id') || pm.get('albumId') || undefined;
      const songId = pm.get('songId');
      if (songId) {
        this.isEdit = true;
        this.songId = songId;
        this.api.getSong(this.songId).subscribe({
          next: (el) => this.form.patchValue(el),
          error: () => alert('Error while fetching Song')
        });
      }
    });
  }

  submit() {
    if (!this.albumId) return alert('No album');
    if (this.form.invalid) return;
    const payload: Song = this.form.value;

    if (this.isEdit && this.songId) {
      this.api.updateSong(this.songId, payload).subscribe({
        next: () => this.router.navigate(['/albums', this.albumId]),
        error: () => alert('Error while updating song')
      });
    } else {
      this.api.addSong(payload).subscribe({
        next: () => this.router.navigate(['/albums', this.albumId]),
        error: () => alert('Errow while adding Song')
      });
    }
  }

  cancel() {
    this.router.navigate(['/albums', this.albumId]);
  }
}
