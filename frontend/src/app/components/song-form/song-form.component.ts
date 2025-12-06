import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-song-form',
    standalone: true,
    imports: [ReactiveFormsModule, CommonModule],
    templateUrl: './song-form.component.html'
})
export class SongFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  albumId!: string;
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
      seconds: ['', Validators.required]
    });

    this.albumId = this.route.snapshot.paramMap.get('albumId')!;
    const songId = this.route.snapshot.paramMap.get('songId');
    if (songId) {
      this.isEdit = true;
      this.songId = songId;

      this.api.getSong(songId).subscribe({
        next: song => this.form.patchValue({
          name: song.name,
          seconds: song.seconds
        }),
        error: () => alert('Error while fetching song')
      });
    }
  }

  submit() {
    if (this.form.invalid) return;

    const payload = {
      ...this.form.value,
      albumId: this.isEdit ? undefined : this.albumId
    };

    if (this.isEdit && this.songId) {
      this.api.updateSong(this.songId, payload).subscribe({
        next: () => this.router.navigate(['/albums', this.albumId]),
        error: () => alert('Error while updating song')
      });
    } else {
      this.api.addSong(payload).subscribe({
        next: () => this.router.navigate(['/albums', this.albumId]),
        error: () => alert('Error while adding song')
      });
    }
  }

  cancel() {
    this.router.navigate(['/albums', this.albumId]);
  }
}
