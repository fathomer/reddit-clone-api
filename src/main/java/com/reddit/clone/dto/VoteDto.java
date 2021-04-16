package com.reddit.clone.dto;

import com.reddit.clone.enums.VoteTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDto {
    private VoteTypeEnum voteType;
    private Long postId;
}