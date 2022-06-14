package com.fluxit.test.springboot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluxit.test.springboot.controller.ApiController;
import com.fluxit.test.springboot.controller.ValidatorControllerAdvice;
import com.fluxit.test.springboot.dto.AuthResponseDto;
import com.fluxit.test.springboot.dto.CandidateDto;
import com.fluxit.test.springboot.dto.CandidateResponseDto;
import com.fluxit.test.springboot.dto.SearchResponseDto;
import com.fluxit.test.springboot.dto.UserDto;
import com.fluxit.test.springboot.entity.Candidate;
import com.fluxit.test.springboot.entity.User;
import com.fluxit.test.springboot.repository.ICandidateRepository;
import com.fluxit.test.springboot.repository.IUserRepository;
import com.fluxit.test.springboot.service.CandidateService;
import com.fluxit.test.springboot.service.ICandidateService;
import com.fluxit.test.springboot.service.IUserService;
import com.fluxit.test.springboot.service.UserService;
import com.fluxit.test.springboot.util.IConstants;
import com.fluxit.test.springboot.util.ResponseCodeEnum;
import com.fluxit.test.springboot.util.jwt.JsonWebTokenAuth;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SpringbootFluxitApplicationTests {

	private MockMvc mvc;
	
	@Mock
	private ICandidateRepository candidateRepoMock;
	
	@Mock
	private IUserRepository userRepoMock;
	
	@InjectMocks
	private ICandidateService candidateServ = new CandidateService(candidateRepoMock, new ModelMapper());//new ConvertObject()
	
	@InjectMocks
	private IUserService userServ = new UserService(userRepoMock, new JsonWebTokenAuth(), new BCryptPasswordEncoder());

	@InjectMocks
	private ApiController controller = new ApiController(userServ, candidateServ);

	private JacksonTester<UserDto> jsonUserDto;
	
	private JacksonTester<AuthResponseDto> jsonAuthResponseDto;
	
	private JacksonTester<CandidateDto> jsonCandidateDto;
	
	private JacksonTester<CandidateResponseDto> jsonCandidateResponseDto;
	
	private JacksonTester<SearchResponseDto> jsonSearchResponseDto;

	@BeforeEach
	public void setup() {
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new ValidatorControllerAdvice())
				.build();
	}
	
	@Test
	void authenticate_post_when_user_login_success() throws Exception {
		
		final String jwtTokenForUserName = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmbHV4aXQxIiwiZXhwIjo0MTA1MTM0MDAwLCJpYXQiOjE2NTUwMDI4MDB9.65uFRI7REpyOAJc8czdhzRCEQv8vbQDZPXyI155C604eth3qzchb1K0lz_NVuQ72UlT_BsVruM4RHDuvUCSr8w";
		
		final String userName = "fluxit1";
		
		final String bCryptPass = "$2a$10$mVKksizszA447gmRi2xJeelFfz.Mp0y.y9h4.bOBvXl.nS19htvS6";
		
		User user = new User();
		user.setId(1);
		user.setUserName(userName);
		user.setPass(bCryptPass);
		
		given(userRepoMock.findByUserName(userName)).willReturn(Optional.of(user));
		
		MockHttpServletResponse response = authenticate_post(new UserDto(userName, "1234"));

		AuthResponseDto obj = jsonAuthResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertNotNull(obj);		
		assertEquals(jwtTokenForUserName, obj.getToken());
		assertEquals(ResponseCodeEnum.OK.getCode(), obj.getError());
		assertNull(obj.getMessage());
	}
	
	@Test
	void authenticate_post_when_user_login_fail() throws Exception {
		
		final String userName = "fluxit1";
		
		final String bCryptPass = "$2a$10$mVKksizszA447gmRi2xJeelFfz.Mp0y.y9h4.bOBvXl.nS19htvS6";
		
		User user = new User();
		user.setId(1);
		user.setUserName(userName);
		user.setPass(bCryptPass);
		
		given(userRepoMock.findByUserName(userName)).willReturn(Optional.of(user));
		given(userRepoMock.findByUserName("fluxit2")).willReturn(Optional.empty());
		
		MockHttpServletResponse response = authenticate_post(new UserDto("fluxit1", "12346"));

		AuthResponseDto obj = jsonAuthResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertNotNull(obj);
		assertNull(obj.getToken());
		assertEquals(ResponseCodeEnum.WRONG_PASSWORD.getCode(), obj.getError());		
		assertEquals(ResponseCodeEnum.WRONG_PASSWORD.getMessage(), obj.getMessage());
		
		response = authenticate_post(new UserDto("fluxit2", "1234"));

		obj = jsonAuthResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertNotNull(obj);
		assertNull(obj.getToken());
		assertEquals(ResponseCodeEnum.USER_NOT_FOUND.getCode(), obj.getError());		
		assertEquals(ResponseCodeEnum.USER_NOT_FOUND.getMessage(), obj.getMessage());
	}
	
	@Test
	void authenticate_post_when_user_fields_are_invalids() throws Exception {

		MockHttpServletResponse response = authenticate_post(new UserDto("", ""));
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_USER_NAME);
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_PASSWORD);
		
		response = authenticate_post(new UserDto("uu", ""));
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_USER_NAME);
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_PASSWORD);
		
		response = authenticate_post(new UserDto("uuu", ""));
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_PASSWORD);
		assertThat(response.getContentAsString()).doesNotContain(IConstants.ERROR_INVALID_USER_NAME);
		
		response = authenticate_post(new UserDto("", "12"));
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_USER_NAME);
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_PASSWORD);
		
		response = authenticate_post(new UserDto("", "1234"));
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_USER_NAME);
		assertThat(response.getContentAsString()).doesNotContain(IConstants.ERROR_INVALID_PASSWORD);
		
		response = authenticate_post(new UserDto("uu", "12"));
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_USER_NAME);
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_PASSWORD);
		
		response = authenticate_post(new UserDto(null, null));
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_USER_NAME);
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_PASSWORD);
		
		response = authenticate_post(new UserDto(null, "1234"));
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_USER_NAME);
		assertThat(response.getContentAsString()).doesNotContain(IConstants.ERROR_INVALID_PASSWORD);
		
		response = authenticate_post(new UserDto("eeee", null));
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_PASSWORD);
		assertThat(response.getContentAsString()).doesNotContain(IConstants.ERROR_INVALID_USER_NAME);
		
		String maxValidUserNamePlus1 = new String(new char[IConstants.USER_NAME_MAX_LENGTH + 1]).replace("\0", "a");
		
		response = authenticate_post(new UserDto(maxValidUserNamePlus1, "1234"));

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_USER_NAME);
		assertThat(response.getContentAsString()).doesNotContain(IConstants.ERROR_INVALID_PASSWORD);
		
		String maxValidUserName = new String(new char[IConstants.USER_NAME_MAX_LENGTH]).replace("\0", "a");
		
		response = authenticate_post(new UserDto(maxValidUserName, "1234"));

		AuthResponseDto obj = jsonAuthResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.USER_NOT_FOUND.getCode(), obj.getError());
		assertNull(obj.getToken());
		
		String maxValidUserNameLess1 = new String(new char[IConstants.USER_NAME_MAX_LENGTH - 1]).replace("\0", "a");
		
		response = authenticate_post(new UserDto(maxValidUserNameLess1, "1234"));

		obj = jsonAuthResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.USER_NOT_FOUND.getCode(), obj.getError());
		assertNull(obj.getToken());
		
		String maxValidPassPlus1 = new String(new char[IConstants.PASS_MAX_LENGTH + 1]).replace("\0", "a");
		
		response = authenticate_post(new UserDto("uuuu", maxValidPassPlus1));

		assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.getContentAsString()).contains(IConstants.ERROR_INVALID_PASSWORD);
		assertThat(response.getContentAsString()).doesNotContain(IConstants.ERROR_INVALID_USER_NAME);
		
		String maxValidPass = new String(new char[IConstants.PASS_MAX_LENGTH]).replace("\0", "a");
		
		response = authenticate_post(new UserDto("uuuu", maxValidPass));

		obj = jsonAuthResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.USER_NOT_FOUND.getCode(), obj.getError());
		assertNull(obj.getToken());
		
		String maxValidPassLess1 = new String(new char[IConstants.PASS_MAX_LENGTH - 1]).replace("\0", "a");
		
		response = authenticate_post(new UserDto("uuuu", maxValidPassLess1));

		obj = jsonAuthResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.USER_NOT_FOUND.getCode(), obj.getError());
		assertNull(obj.getToken());
	}
	
	@Test
	void candidate_post_when_add_new_candidate_fail_due_wrong_auth_bearer() throws Exception {
		
		final String authBear = "wrong";
		
		CandidateDto candidateDto = new CandidateDto();
		candidateDto.setAddress("Dir 1");
		candidateDto.setDateBirth(new Date());
		candidateDto.setDni(1111);
		candidateDto.setEmail("email@gmail.com");
		candidateDto.setName("Name 1");
		candidateDto.setSurName("Last Name 1");
		candidateDto.setPhoneNumber("+5402222");
				
		MockHttpServletResponse response = add_candidate_post(candidateDto, authBear);
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
	}
	
	@Test
	void candidate_post_when_add_new_candidate_success() throws Exception {
		
		final String authBear = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmbHV4aXQxIiwiZXhwIjo0MTA1MTM0MDAwLCJpYXQiOjE2NTUwMDI4MDB9.65uFRI7REpyOAJc8czdhzRCEQv8vbQDZPXyI155C604eth3qzchb1K0lz_NVuQ72UlT_BsVruM4RHDuvUCSr8w";
		
		final String userName = "fluxit1";
		
		final String bCryptPass = "$2a$10$mVKksizszA447gmRi2xJeelFfz.Mp0y.y9h4.bOBvXl.nS19htvS6";
		
		User user = new User();
		user.setId(1);
		user.setUserName(userName);
		user.setPass(bCryptPass);
		
		Candidate candidate = new Candidate();		
		candidate.setDateBirth(new Date());
		candidate.setDni(1111);
		candidate.setEmail("email@gmail.com");
		candidate.setName("Name 1");
		candidate.setSurName("Last Name 1");		
		candidate.setAddress("Dir 1");		
		candidate.setPhoneNumber("+5402222");
		
		given(userRepoMock.findByUserName(userName)).willReturn(Optional.of(user));
		given(candidateRepoMock.findById(1111)).willReturn(Optional.empty());
		given(candidateRepoMock.save(any())).willReturn(candidate);
				
		CandidateDto candidateDto = new CandidateDto();
		candidateDto.setAddress("Dir 1");
		candidateDto.setDateBirth(new Date());
		candidateDto.setDni(1111);
		candidateDto.setEmail("email@gmail.com");
		candidateDto.setName("Name 1");
		candidateDto.setSurName("Last Name 1");
		candidateDto.setPhoneNumber("+5402222");
				
		MockHttpServletResponse response = add_candidate_post(candidateDto, authBear);
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
		CandidateResponseDto obj = jsonCandidateResponseDto.parseObject(response.getContentAsString());
		
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.OK.getCode(), obj.getError());
		assertNotNull(obj.getCandidate());
	}

	@Test
	void candidate_post_when_add_new_candidate_fail_due_dni_exist() throws Exception {
		
		final String authBear = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmbHV4aXQxIiwiZXhwIjo0MTA1MTM0MDAwLCJpYXQiOjE2NTUwMDI4MDB9.65uFRI7REpyOAJc8czdhzRCEQv8vbQDZPXyI155C604eth3qzchb1K0lz_NVuQ72UlT_BsVruM4RHDuvUCSr8w";
		
		final String userName = "fluxit1";
		
		final String bCryptPass = "$2a$10$mVKksizszA447gmRi2xJeelFfz.Mp0y.y9h4.bOBvXl.nS19htvS6";
		
		User user = new User();
		user.setId(1);
		user.setUserName(userName);
		user.setPass(bCryptPass);
		
		Candidate candidate = new Candidate();		
		candidate.setDateBirth(new Date());
		candidate.setDni(1111);
		candidate.setEmail("email@gmail.com");
		candidate.setName("Name 1");
		candidate.setSurName("Last Name 1");		
		candidate.setAddress("Dir 1");		
		candidate.setPhoneNumber("+5402222");
		
		given(userRepoMock.findByUserName(userName)).willReturn(Optional.of(user));
		given(candidateRepoMock.findById(candidate.getDni())).willReturn(Optional.of(candidate));
				
		CandidateDto candidateDto = new CandidateDto();
		candidateDto.setAddress("Dir 1");
		candidateDto.setDateBirth(new Date());
		candidateDto.setDni(1111);
		candidateDto.setEmail("email@gmail.com");
		candidateDto.setName("Name 1");
		candidateDto.setSurName("Last Name 1");
		candidateDto.setPhoneNumber("+5402222");
				
		MockHttpServletResponse response = add_candidate_post(candidateDto, authBear);
		
		CandidateResponseDto obj = jsonCandidateResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.CANDIDATE_EXIST.getCode(), obj.getError());
		assertNotNull(obj.getCandidate());
		verify(candidateRepoMock).findById(obj.getCandidate().getDni());
	}
	
	@Test
	void candidate_put_when_edit_candidate_success() throws Exception {
		
		final String authBear = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmbHV4aXQxIiwiZXhwIjo0MTA1MTM0MDAwLCJpYXQiOjE2NTUwMDI4MDB9.65uFRI7REpyOAJc8czdhzRCEQv8vbQDZPXyI155C604eth3qzchb1K0lz_NVuQ72UlT_BsVruM4RHDuvUCSr8w";
		
		final String userName = "fluxit1";
		
		final String bCryptPass = "$2a$10$mVKksizszA447gmRi2xJeelFfz.Mp0y.y9h4.bOBvXl.nS19htvS6";
		
		User user = new User();
		user.setId(1);
		user.setUserName(userName);
		user.setPass(bCryptPass);
		
		final Date dateBirth = new Date();
		
		Candidate candidate = new Candidate();		
		candidate.setDateBirth(dateBirth);
		candidate.setDni(1111);
		candidate.setEmail("email@gmail.com");
		candidate.setName("Name 1");
		candidate.setSurName("Last Name 1");		
		candidate.setAddress("Dir 1");		
		candidate.setPhoneNumber("+5402222");
		
		Candidate candidate2 = new Candidate();		
		candidate2.setDateBirth(dateBirth);
		candidate2.setDni(1111);
		candidate2.setEmail("email2@gmail.com");
		candidate2.setName("Name 1");
		candidate2.setSurName("Last Name 1");		
		candidate2.setAddress("Dir 2");		
		candidate2.setPhoneNumber("+5402222");
		
		given(userRepoMock.findByUserName(userName)).willReturn(Optional.of(user));
		given(candidateRepoMock.findById(1111)).willReturn(Optional.of(candidate));
		//given(candidateRepoMock.save(candidate)).willReturn(candidate2);
						
		CandidateDto candidateDto = new ModelMapper().map(candidate2, CandidateDto.class);
				
		MockHttpServletResponse response = edit_candidate_put(candidateDto, 1111, authBear);
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
		CandidateResponseDto obj = jsonCandidateResponseDto.parseObject(response.getContentAsString());
		
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.OK.getCode(), obj.getError());
		assertNotNull(obj.getCandidate());
		assertEquals("Dir 2", obj.getCandidate().getAddress());
		assertEquals("email2@gmail.com", obj.getCandidate().getEmail());
		assertEquals("Name 1", obj.getCandidate().getName());
		assertEquals("Last Name 1", obj.getCandidate().getSurName());
		assertEquals("+5402222", obj.getCandidate().getPhoneNumber());
	}
	
	@Test
	void candidate_delete_when_delete_success() throws Exception {
		
		final String authBear = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmbHV4aXQxIiwiZXhwIjo0MTA1MTM0MDAwLCJpYXQiOjE2NTUwMDI4MDB9.65uFRI7REpyOAJc8czdhzRCEQv8vbQDZPXyI155C604eth3qzchb1K0lz_NVuQ72UlT_BsVruM4RHDuvUCSr8w";
		
		final String userName = "fluxit1";
		
		final String bCryptPass = "$2a$10$mVKksizszA447gmRi2xJeelFfz.Mp0y.y9h4.bOBvXl.nS19htvS6";
		
		User user = new User();
		user.setId(1);
		user.setUserName(userName);
		user.setPass(bCryptPass);
		
		final Date dateBirth = new Date();
		
		Candidate candidate = new Candidate();		
		candidate.setDateBirth(dateBirth);
		candidate.setDni(1111);
		candidate.setEmail("email@gmail.com");
		candidate.setName("Name 1");
		candidate.setSurName("Last Name 1");		
		candidate.setAddress("Dir 1");		
		candidate.setPhoneNumber("+5402222");
		
		given(userRepoMock.findByUserName(userName)).willReturn(Optional.of(user));
		
		given(candidateRepoMock.findById(1111)).willReturn(Optional.of(candidate));
		given(candidateRepoMock.findById(1112)).willReturn(Optional.empty());
		
		MockHttpServletResponse response = delete_candidate_delete(1111, authBear);
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
		CandidateResponseDto obj = jsonCandidateResponseDto.parseObject(response.getContentAsString());
		
		verify(candidateRepoMock).delete(eq(candidate));
		
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.OK.getCode(), obj.getError());
		assertNull(obj.getCandidate());
		assertNull(obj.getMessage());
		
		response = delete_candidate_delete(1112, authBear);
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
		
		obj = jsonCandidateResponseDto.parseObject(response.getContentAsString());
		
		verify(candidateRepoMock).delete(eq(candidate));
		
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.CANDIDATE_NOT_EXIST.getCode(), obj.getError());
		assertNull(obj.getCandidate());
		assertNotNull(obj.getMessage());
		assertEquals(ResponseCodeEnum.CANDIDATE_NOT_EXIST.getMessage(), obj.getMessage());
	}
	
	@Test
	void candidate_get_when_search_by_dni_or_name_and_surname_success() throws Exception {
		
		final String authBear = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmbHV4aXQxIiwiZXhwIjo0MTA1MTM0MDAwLCJpYXQiOjE2NTUwMDI4MDB9.65uFRI7REpyOAJc8czdhzRCEQv8vbQDZPXyI155C604eth3qzchb1K0lz_NVuQ72UlT_BsVruM4RHDuvUCSr8w";
		
		final String userName = "fluxit1";
		
		final String bCryptPass = "$2a$10$mVKksizszA447gmRi2xJeelFfz.Mp0y.y9h4.bOBvXl.nS19htvS6";
		
		User user = new User();
		user.setId(1);
		user.setUserName(userName);
		user.setPass(bCryptPass);

		final String page = IConstants.DEFAULT_PAGE;
		
		final String pageSize = IConstants.DEFAULT_PAGE_SIZE;
		
		final Pageable pageable = PageRequest.of(Integer.valueOf(page), Integer.valueOf(pageSize), Sort.by("dni").ascending());
		
		final int searcDni = 0;
		
		final String searchName = "Name 1";
		
		final String searchSurName = "Last";

		PageImpl<Candidate> filterPage = new PageImpl<>(getPageCandidatesList().stream().filter(c-> c.getDni() == searcDni || (c.getName().startsWith(searchName) && c.getSurName().startsWith(searchSurName))).collect(Collectors.toList()));
		
		given(userRepoMock.findByUserName(userName)).willReturn(Optional.of(user));
		
		given(candidateRepoMock.findByDniOrNameAndSurName(searcDni, searchName+"%", searchSurName+"%", pageable)).willReturn(filterPage);
		
		MockHttpServletResponse response =  search_candidate_by_dni_or_name_and_surname_get(searcDni, searchName, searchSurName, page, pageSize, authBear);
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
		SearchResponseDto obj = jsonSearchResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.OK.getCode(), obj.getError());
		assertNotNull(obj.getCandidates());
		assertEquals(1, obj.getCandidates().size());
	}
	
	@Test
	void candidate_get_when_get_candidate_info_success() throws Exception {
		
		final String authBear = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmbHV4aXQxIiwiZXhwIjo0MTA1MTM0MDAwLCJpYXQiOjE2NTUwMDI4MDB9.65uFRI7REpyOAJc8czdhzRCEQv8vbQDZPXyI155C604eth3qzchb1K0lz_NVuQ72UlT_BsVruM4RHDuvUCSr8w";
		
		final String userName = "fluxit1";
		
		final String bCryptPass = "$2a$10$mVKksizszA447gmRi2xJeelFfz.Mp0y.y9h4.bOBvXl.nS19htvS6";
		
		User user = new User();
		user.setId(1);
		user.setUserName(userName);
		user.setPass(bCryptPass);

		final int searcDni = 0;
		
		final Candidate candidate = getPageCandidatesList().getContent().get(0);
		
		given(userRepoMock.findByUserName(userName)).willReturn(Optional.of(user));
		
		given(candidateRepoMock.findById(searcDni)).willReturn(Optional.of(candidate));
		
		MockHttpServletResponse response =  get_candidate_by_dni_get(searcDni, authBear);
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
		CandidateResponseDto obj = jsonCandidateResponseDto.parseObject(response.getContentAsString());
		
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		assertNotNull(obj);
		assertEquals(ResponseCodeEnum.OK.getCode(), obj.getError());
		assertNotNull(obj.getCandidate());
	}
	
	private MockHttpServletResponse authenticate_post(UserDto user) throws Exception {
		
		return mvc.perform(post("/fluxit/v1/auth")
				.content(jsonUserDto.write(user).getJson())
				.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))
				.accept(new MediaType("application", "json", StandardCharsets.UTF_8)))
				.andReturn()
				.getResponse();
	}
	
	private MockHttpServletResponse add_candidate_post(CandidateDto candidate, String authorization) throws Exception {
		
		return mvc.perform(post("/fluxit/v1/candidate")				
				.content(jsonCandidateDto.write(candidate).getJson())
				.header("Authorization", "Bearer "+authorization)
				.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))				
				.accept(new MediaType("application", "json", StandardCharsets.UTF_8)))
				.andReturn()
				.getResponse();
	}
	
	private MockHttpServletResponse edit_candidate_put(CandidateDto candidate, int dni, String authorization) throws Exception {
		
		return mvc.perform(put("/fluxit/v1/candidate/"+dni)				
				.content(jsonCandidateDto.write(candidate).getJson())
				.header("Authorization", "Bearer "+authorization)
				.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))				
				.accept(new MediaType("application", "json", StandardCharsets.UTF_8)))
				.andReturn()
				.getResponse();
	}
	
	private MockHttpServletResponse delete_candidate_delete(int dni, String authorization) throws Exception {
		
		return mvc.perform(delete("/fluxit/v1/candidate/"+dni)				
				.header("Authorization", "Bearer "+authorization)
				.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))				
				.accept(new MediaType("application", "json", StandardCharsets.UTF_8)))
				.andReturn()
				.getResponse();
	}
	
	private MockHttpServletResponse search_candidate_by_dni_or_name_and_surname_get(int dni, String name, String surName, String page, String pageSize, String authorization) throws Exception {
		
		return mvc.perform(get("/fluxit/v1/candidate")				
				.param("dni", String.valueOf(dni))
				.param("name", name)
				.param("surName", surName)
				.param("page", page)
				.param("pageSize", pageSize)
				.header("Authorization", "Bearer "+authorization)
				.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))				
				.accept(new MediaType("application", "json", StandardCharsets.UTF_8)))
				.andReturn()
				.getResponse();
	}
	
	private MockHttpServletResponse get_candidate_by_dni_get(int dni, String authorization) throws Exception {
		
		return mvc.perform(get("/fluxit/v1/candidate/"+dni)				
				.header("Authorization", "Bearer "+authorization)
				.contentType(new MediaType("application", "json", StandardCharsets.UTF_8))				
				.accept(new MediaType("application", "json", StandardCharsets.UTF_8)))
				.andReturn()
				.getResponse();
	}
	
	private Page<Candidate> getPageCandidatesList() {
		
		List<Candidate> candidates = new ArrayList<>();
		
		Candidate candidate = new Candidate();		
		candidate.setDateBirth(new Date());
		candidate.setDni(1111);
		candidate.setEmail("email@gmail.com");
		candidate.setName("Name 1");
		candidate.setSurName("Last Name 1");		
		candidate.setAddress("Dir 1");		
		candidate.setPhoneNumber("+5402222");		
		candidates.add(candidate);
		
		candidate = new Candidate();		
		candidate.setDateBirth(new Date());
		candidate.setDni(22222);
		candidate.setEmail("email@gmail.com");
		candidate.setName("Name 2");
		candidate.setSurName("Last Name 2");		
		candidate.setAddress("Dir 1");		
		candidate.setPhoneNumber("+5402222");		
		candidates.add(candidate);

		candidate = new Candidate();		
		candidate.setDateBirth(new Date());
		candidate.setDni(33333);
		candidate.setEmail("email@gmail.com");
		candidate.setName("Name 3");
		candidate.setSurName("Last Name 3");		
		candidate.setAddress("Dir 1");		
		candidate.setPhoneNumber("+5402222");		
		candidates.add(candidate);
		
		return new PageImpl<>(candidates);
	}
}
