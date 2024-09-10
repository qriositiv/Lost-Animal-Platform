export interface Comment {
  author: string;
  authorImage: string;
  authorRole: string;
  publicationDate: string;
  comment: string;
}

export interface CommentResponse {
  commentId: string;
  reportId: string;
  username: string;
  role: string;
  comment: string;
  createdAt: [number, number, number, number, number, number, number];
}

export interface CreateCommentRequest {
  reportId: string;
  comment: string;
}
