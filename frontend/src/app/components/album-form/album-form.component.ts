import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-album-form',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './album-form.component.html'
})
export class AlbumFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  albumId?: string;

  constructor(
    private fb: FormBuilder,
    private api: ApiService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required],
      author: ['', Validators.required],
      yearOfRelease: ['', Validators.required]
    });

    this.route.paramMap.subscribe(pm => {
      const albumId = pm.get('albumId');
      if (albumId) {
        this.isEdit = true;
        this.albumId = albumId;
        this.api.getAlbum(albumId).subscribe({
          next: cat => this.form.patchValue(cat),
          error: () => alert('error while fetching album')
        });
      }
    });
  }

  submit() {
    if (this.form.invalid) return;
    const payload = this.form.value;

    if (this.isEdit && this.albumId) {
      this.api.updateAlbum(this.albumId, payload).subscribe({
        next: () => this.router.navigate(['/albums']),
        error: () => alert('error while updating')
      });
    } else {
      this.api.addAlbum(payload).subscribe({
        next: () => this.router.navigate(['/albums']),
        error: () => alert('error while adding')
      });
    }
  }

  cancel() {
    this.router.navigate(['/albums']);
  }
}
