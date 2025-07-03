export interface PostMetadataDto {
  postUniqueId: number;
  imageUrl: string;
  description: string;
  upVote: number;
  downVote: number;
  voteSum: number;
  createdAt: string;
  // Based on backend code, these should be included
  user?: {
    id: string;
    pseudo: string;
    profileImg: string | null;
  };
}

export interface PostMetadataWithUserDto extends PostMetadataDto {
  user: {
    id: string;
    pseudo: string;
    profileImg: string | null;
  };
}

export interface PostPageDto {
  content: PostMetadataDto[];
  totalPages: number;
  number: number;
  totalElements: number;
}
