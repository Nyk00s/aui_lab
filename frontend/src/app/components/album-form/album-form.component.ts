import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../../services/api.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-album-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './album-form.component.html'
})
export class AlbumFormComponent implements OnInit {
  form!: FormGroup;
  isEdit = false;
  catId?: string;

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
      const id = pm.get('id');
      if (id) {
        this.isEdit = true;
        this.catId = id;
        this.api.getAlbum(id).subscribe({
          next: cat => this.form.patchValue(cat),
          error: () => alert('error while fetching album')
        });
      }
    });
  }

  submit() {
    if (this.form.invalid) return;
    const payload = this.form.value;

    if (this.isEdit && this.catId) {
      this.api.updateAlbum(this.catId, payload).subscribe({
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
