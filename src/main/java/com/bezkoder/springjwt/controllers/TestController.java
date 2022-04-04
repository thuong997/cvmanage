package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.Service.UserService;
import com.bezkoder.springjwt.Utils.ErrorCode;
import com.bezkoder.springjwt.security.CustomException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/test")
@RestController
@AllArgsConstructor
public class TestController {
	private final UserService userService;

//	@PostMapping("/user/list")
//	public ResponseEntity<List<UserResponse>> importProviderCompany(@RequestParam("userName") String userName ,@RequestParam("email") String email) throws IOException {
//		List<UserResponse> response = userService.getUser(userName,email);
//		return new ResponseEntity<>(response, HttpStatus.OK);
//	}

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('ADMIN')")
//	@ExceptionHandler({ AuthenticationException.class })
	public String userAccess() {
		throw new CustomException(HttpStatus.BAD_REQUEST)
				.addError(ErrorCode.ERR_SESSSION_EXPIRE)
				.withMessage(ErrorCode.ERR_SESSSION_EXPIRE.getMessage());
//	    return "User Content.";
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}
