package com.guardedbox.dto;

import static com.guardedbox.constants.Constraints.BASE64_PATTERN;
import static com.guardedbox.constants.Constraints.ENCRYPTED_KEY_LENGTH;
import static com.guardedbox.constants.Constraints.GROUP_NAME_MAX_LENGTH;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO: Body of the create secret request.
 *
 * @author s3curitybug@gmail.com
 *
 */
@Getter
@Setter
public class CreateGroupDto
        implements Serializable {

    /** Serial Version UID. */
    private static final long serialVersionUID = -1781168530115693520L;

    /** Name. */
    @NotBlank
    @Size(max = GROUP_NAME_MAX_LENGTH)
    private String name;

    /** Encrypted Key. */
    @NotBlank
    @Pattern(regexp = BASE64_PATTERN)
    @Size(min = ENCRYPTED_KEY_LENGTH, max = ENCRYPTED_KEY_LENGTH)
    private String encryptedGroupKey;

}