export interface PostMetadataDto {
  postUniqueId: number;
  imageUrl: string;
  description: string;
  upVote: number;
  downVote: number;
  voteSum: number;
  createdAt: string;
}

export interface PostPageDto {
  content: PostMetadataDto[];
  totalPages: number;
  number: number;
  totalElements: number;
}
