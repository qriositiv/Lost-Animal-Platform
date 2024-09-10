import { Component, Inject, Input, LOCALE_ID, OnDestroy, inject } from '@angular/core';
import { CommentResponse } from '../../../../../interfaces/comment.interface';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faLanguage, faTrash } from '@fortawesome/free-solid-svg-icons';
import { FormatDateArrayPipe } from '../../../../../pipes/format-date.pipe';
import { FormGroup, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserService } from '../../../../../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { PublicService } from '../../../../../services/public.service';
import { AuthService } from '../../../../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-comments',
  standalone: true,
  imports: [FontAwesomeModule, FormatDateArrayPipe, ReactiveFormsModule],
  templateUrl: './comments.component.html',
})
export class CommentsComponent implements OnDestroy {
  @Input() allComments!: Array<CommentResponse>;
  @Input() reportId!: string;
  faTranslate = faLanguage;
  faDelete = faTrash;

  translated: boolean[] = [];
  translatedKeep: string[] = [];
  username!: string;
  role!: string;

  newCommentForm: FormGroup;
  subscription: Subscription | null = null;
  publicSubscription: Subscription | null = null;
  publicTranslateSubscription: Subscription | null = null;

  service = inject(UserService);
  toastr = inject(ToastrService);
  authService = inject(AuthService);
  publicService = inject(PublicService);
  locale: string = inject(LOCALE_ID);

  openProfile(username: string): void {
    this.router.navigate([username, `profile`]);
  }

  constructor(private fb: FormBuilder, private router: Router) {
    this.newCommentForm = this.fb.group({
      comment: ['', []],
    });
  }

  ngOnInit() {
    const token = this.authService.decodeToken();
    if (!token) return;
    this.username = token.username;
    this.role = token.role;
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    if (this.publicSubscription) {
      this.publicSubscription.unsubscribe();
    }
  }

  onSubmit(): void {
    const commentMessage = this.newCommentForm.get('comment')?.value;
    this.subscription = this.service.postCommentByReportId(this.reportId, commentMessage).subscribe({
      next: (response) => {
        this.toastr.success('You have sucessfully created a comment.', 'Success!');
        this.fetchNewComments();
        this.newCommentForm.patchValue({
          comment: '',
        });
      },
      error: (error) => {
        this.toastr.error('Failed creating comment' + error.error.text, 'Error!');
        console.log(error);
      },
    });
  }

  fetchNewComments(): void {
    this.subscription = this.publicService.getAllCommentsByReportId(this.reportId).subscribe({
      next: (response) => {
        this.allComments = response;
      },
      error: (error) => {
        this.toastr.error('Failed fetching new comments' + error.error.text, 'Error!');
        console.log(error);
      },
    });
  }

  authorImageExample: string = 'https://randomuser.me/api/portraits/men/1.jpg';

  translateNotes(comment: CommentResponse, index: number): void {
    this.translated[index] = !this.translated[index];
    if (this.translatedKeep[index]) {
      const temp = comment.comment;
      comment.comment = this.translatedKeep[index];
      this.translatedKeep[index] = temp;
    } else {
      this.translatedKeep[index] = comment.comment;
      this.publicTranslateSubscription = this.publicService.translate(comment.comment, 'auto', this.locale).subscribe({
        next: (response) => {
          comment.comment = response.translatedText;
        },
        error: (error) => {
          this.toastr.error('Failed fetching ' + error.error.text, 'Error!');
          console.log(error);
        },
      });
    }
  }

  onDelete(commentId: string): void {
    this.subscription = this.service.deleteCommentByReportId(this.reportId, commentId).subscribe({
      next: (response) => {
        this.toastr.success('You have sucessfully deleted a comment.', 'Success!');
        this.fetchNewComments();
      },
      error: (error) => {
        this.toastr.error('Failed deleting comment' + error.error.text, 'Error!');
        console.log(error);
      },
    });
  }
}
