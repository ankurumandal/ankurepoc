package com.ascent.autobcm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ascent.autobcm.exception.EmployeeIdAlreadyPresentException;
import com.ascent.autobcm.exception.EntityAlreadyPresentException;
import com.ascent.autobcm.exception.EntityAttributeAlreadyPresentException;
import com.ascent.autobcm.exception.EntityTypeAlreadyPresentException;
import com.ascent.autobcm.exception.EntityTypeAttributeDefinitionAlreadyPresent;
import com.ascent.autobcm.exception.GroupAlreadyPresentException;
import com.ascent.autobcm.exception.ImproperParametersPassedException;
import com.ascent.autobcm.exception.ImproperPasswordException;
import com.ascent.autobcm.exception.ImproperPasswordLengthException;
import com.ascent.autobcm.exception.NoPermissionFoundException;
import com.ascent.autobcm.exception.NoSuchEntityAttributeFoundException;
import com.ascent.autobcm.exception.NoSuchEntityFound;
import com.ascent.autobcm.exception.NoSuchEntityTypeAttributeDefinitionFound;
import com.ascent.autobcm.exception.NoSuchEntityTypeFound;
import com.ascent.autobcm.exception.NoSuchGroupFound;
import com.ascent.autobcm.exception.NoSuchOperationFound;
import com.ascent.autobcm.exception.NoSuchRoleFoundException;
import com.ascent.autobcm.exception.OperationAlreadyPresentException;
import com.ascent.autobcm.exception.PermissionAlreadyExistsException;
import com.ascent.autobcm.exception.RoleAlreadyPresentException;
import com.ascent.autobcm.exception.SystemAdminIntegrityCheckException;
import com.ascent.autobcm.exception.UserNameAlreadyPresent;
import com.ascent.autobcm.model.Entities;
import com.ascent.autobcm.model.EntitiesAttribute;
import com.ascent.autobcm.model.EntitiesType;
import com.ascent.autobcm.model.EntityAttributeDefinition;
import com.ascent.autobcm.model.EntityAttributeValues;
import com.ascent.autobcm.model.EntityTypeAttributeDefinition;
import com.ascent.autobcm.model.Group;
import com.ascent.autobcm.model.Operations;
import com.ascent.autobcm.model.Permission;
import com.ascent.autobcm.model.Role;
import com.ascent.autobcm.model.RoleGroup;
import com.ascent.autobcm.model.RoleOperations;
import com.ascent.autobcm.model.User;
import com.ascent.autobcm.model.UserGroup;
import com.ascent.autobcm.model.UserRole;
import com.ascent.autobcm.service.EntitiesAttributeService;
import com.ascent.autobcm.service.EntitiesService;
import com.ascent.autobcm.service.EntitiesTypeService;
import com.ascent.autobcm.service.EntityAttributeDefinitionService;
import com.ascent.autobcm.service.EntityAttributeValueService;
import com.ascent.autobcm.service.EntityTypeAttributeDefinitionService;
import com.ascent.autobcm.service.GroupService;
import com.ascent.autobcm.service.OperationsService;
import com.ascent.autobcm.service.PermissionService;
import com.ascent.autobcm.service.RoleGroupService;
import com.ascent.autobcm.service.RoleOperationsService;
import com.ascent.autobcm.service.RoleService;
import com.ascent.autobcm.service.UserGroupService;
import com.ascent.autobcm.service.UserRoleService;
import com.ascent.autobcm.service.UserService;
import com.ascent.autobcm.util.Constants;
import com.google.api.translate.Language;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@RestController
@RequestMapping("/rbac/rest/")
public class RbacRestController {

	@Autowired
	UserService userService;

	@Autowired
	RoleService roleService;

	@Autowired
	UserRoleService userRoleService;

	@Autowired
	OperationsService operationsService;

	@Autowired
	RoleOperationsService roleOperationsService;

	@Autowired
	GroupService groupService;

	@Autowired
	RoleGroupService roleGroupService;

	@Autowired
	UserGroupService userGroupService;

	@Autowired
	EntitiesService entityService;

	@Autowired
	EntitiesTypeService entityTypeService;

	@Autowired
	EntitiesAttributeService entityAttributeService;

	@Autowired
	EntityAttributeValueService entityAttributeValueService;

	@Autowired
	EntityAttributeDefinitionService entityAttributeDefinitionService;

	@Autowired
	EntityTypeAttributeDefinitionService entityTypeAttributeService;

	@Autowired
	PermissionService permissionService;

	private static final Logger LOGGER = LogManager.getLogger(RbacRestController.class);

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}

	// User Related Start
	@PostMapping(value = "/registerUser", consumes = "application/json", produces = "application/json")
	public ResponseEntity<User> registerNewUserJson(@RequestBody User user) throws Exception {
		LOGGER.info("Entering /registerUser Mapping --> registerNewUserJson method");

		User persistedUser = null;
		try {
			persistedUser = userService.saveUser(user);
		} catch (UserNameAlreadyPresent | EmployeeIdAlreadyPresentException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exit /registerUser Mapping --> registerNewUserJson method");
		return ResponseEntity.ok(persistedUser);
	}

	@PostMapping(value = "/registerUser/{firstName}/{lastName}/{userName}/{password}/{contactNumber}/{employeeId}", produces = "application/json")
	public User registerNewUser(@PathVariable String firstName, @PathVariable String lastName,
			@PathVariable String userName, @PathVariable String password, @PathVariable String contactNumber,
			@PathVariable String employeeId) throws Exception {

		LOGGER.info("Entering /registerUser Mapping --> registerNewUserJson method");

		User userToPersist = null;

		try {
			if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty() || userName == null
					|| userName.isEmpty() || password == null || password.isEmpty() || contactNumber == null
					|| contactNumber.isEmpty() || employeeId == null || employeeId.isEmpty())
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			if (Long.valueOf(password.length()) < Long.valueOf(Constants.PASSWORD_LENGTH))
				throw new ImproperPasswordLengthException(Constants.INCORRECT_PASSWORD_LENGTH);

			userToPersist = new User();
			userToPersist.setFirstName(firstName);
			userToPersist.setLastName(lastName);
			userToPersist.setPassword(password);
			userToPersist.setContactNumber(contactNumber);
			userToPersist.setUserName(userName);
			userToPersist.setActive(Constants.ACTIVE);
			userToPersist.setEmployeeId(employeeId);
			userToPersist.setUserToPasswordReset(Constants.ACTIVE);

			userToPersist = userService.saveUser(userToPersist);
		} catch (UserNameAlreadyPresent | EmployeeIdAlreadyPresentException | ImproperPasswordException
				| ImproperParametersPassedException | ImproperPasswordLengthException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting /registerUser Mapping --> registerNewUserJson method");
		return userToPersist;
	}

	@PostMapping(value = "/updateUser/{id}/{firstName}/{lastName}/{userName}/{password}/{contactNumber}/{employeeId}", produces = "application/json")
	public User updateExistingUser(@PathVariable long id, @PathVariable String firstName, @PathVariable String lastName,
			@PathVariable String userName, @PathVariable String password, @PathVariable String contactNumber,
			@PathVariable String employeeId) throws Exception {

		LOGGER.info("Entering /updateUser Mapping --> updateExistingUser method");

		User userToUpdate = null;

		try {

			if (id == 0 || firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()
					|| userName == null || userName.isEmpty() || password == null || password.isEmpty()
					|| contactNumber == null || contactNumber.isEmpty() || employeeId == null || employeeId.isEmpty())
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			userToUpdate = userService.findById(id);

			userToUpdate.setFirstName(firstName);
			userToUpdate.setLastName(lastName);
			userToUpdate.setPassword(password);
			userToUpdate.setContactNumber(contactNumber);
			userToUpdate.setUserName(userName);
//			userToUpdate.setActive(Constants.ACTIVE);
			userToUpdate = userService.saveUser(userToUpdate);

		} catch (UserNameAlreadyPresent | EmployeeIdAlreadyPresentException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting /updateUser Mapping --> updateExistingUser method");
		return userToUpdate;
	}

	@DeleteMapping(value = "/deleteUser/{userId}")
	public ResponseEntity<User> deleteExistingUser(@PathVariable long userId) throws Exception {
		LOGGER.info("Entering /DeleteUser Mapping --> deleteUser method");

		User user = null;

		try {

			if (userId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			user = userService.deleteUser(userId);

		} catch (SystemAdminIntegrityCheckException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting deleteExistingUser Method");
		return ResponseEntity.ok(user);

	}

	@PostMapping("/reactivateUser/{userId}")
	public ResponseEntity<User> reactivateUser(@PathVariable long userId) throws Exception {

		LOGGER.info("Entering reactivateUser Method");

		User user = null;

		try {

			if (userId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			user = userService.reactivateUser(userId);

		} catch (ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting reactivateUser Method");
		return ResponseEntity.ok(user);

	}

	// User Related End

	// Role Related Start
	@PostMapping(value = "/addRole/{roleToBePersisted}", produces = "application/json")
	public Role registerNewRole(@PathVariable String roleToBePersisted) throws Exception {

		LOGGER.info("Entering /registerRole Mapping --> registerNewRole method");

		Role role = null;

		try {

			if (null == roleToBePersisted || roleToBePersisted.isEmpty())
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			role = new Role();
			role.setRoleName(roleToBePersisted);
			role.setActive(Constants.ACTIVE);
			role = roleService.saveRole(role);
		} catch (RoleAlreadyPresentException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Entering /registerRole Mapping --> registerNewRole method");
		return role;
	}

	@PostMapping(value = "/updateRole/{roleId}/{roleToBePersisted}", produces = "application/json")
	public Role updateExistingRole(@PathVariable long roleId, @PathVariable String roleToBePersisted) throws Exception {
		LOGGER.info("Entering /updateRole Mapping --> updateExistingRole method");

		Role role = null;

		try {

			if (null == roleToBePersisted || roleToBePersisted.isEmpty() || roleId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			role = roleService.findById(roleId);
			role.setId(roleId);
			role.setRoleName(roleToBePersisted);
			role = roleService.updateRole(role);
		} catch (RoleAlreadyPresentException | NoSuchRoleFoundException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}
		LOGGER.info("Exiting /updateRole Mapping --> updateExistingRole method");
		return role;
	}

	@DeleteMapping(value = "/DeleteRole/{roleId}", produces = "application/json")
	@Transactional
	public ResponseEntity<Role> deleteExistingRole(@PathVariable long roleId) throws Exception {

		LOGGER.info("Entering /DeleteRole Mapping --> deleteRole method");

		Role roleFromDB = null;

		try {
			if (roleId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			roleFromDB = roleService.deleteRole(roleId);
		} catch (NoSuchRoleFoundException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Entering /DeleteRole Mapping --> deleteRole method");
		return ResponseEntity.ok(roleFromDB);

	}

	@DeleteMapping(value = "/reactivateRole/{roleId}", produces = "application/json")
	public ResponseEntity<Role> reactivateRole(@PathVariable long roleId) throws Exception {

		LOGGER.info("Entering /reactivateRole Mapping --> deleteRole method");

		Role roleFromDB = null;

		try {
			if (roleId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			roleFromDB = roleService.reactivateRole(roleId);
		} catch (NoSuchRoleFoundException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Entering /reactivateRole Mapping --> deleteRole method");
		return ResponseEntity.ok(roleFromDB);

	}
	// Role Related End

	@GetMapping(value = "/fetchOperationsForRole/{roleId}", produces = "application/json")
	public List<Operations> findOperationsForRole(@PathVariable long roleId) {

		LOGGER.info("Entering /fetchOperationsForRole Mapping --> findOperationsForRole method");
		List<Operations> operationsAssigned = roleOperationsService.findByRole(roleId);

		LOGGER.info("Exiting /fetchOperationsForRole Mapping --> findOperationsForRole method");
		return operationsAssigned;
	}

	@GetMapping(value = "/fetchRolesForOperation/{operationId}", produces = "application/json")
	public List<Role> findRolesForOperations(@PathVariable long operationId) {

		LOGGER.info("Entering /fetchRolesForOperation Mapping --> findRolesForOperations method");
		List<Role> rolesAssigned = roleOperationsService.findByOperation(operationId);

		LOGGER.info("Exiting /fetchRolesForOperation Mapping --> findRolesForOperations method");
		return rolesAssigned;
	}

	@PostMapping(value = "/AssignRolesToUser/{employeeId}/{roles}", produces = "application/json")
	public List<UserRole> assignRolesToUser(@PathVariable long employeeId, @PathVariable String roles)
			throws Exception {

		LOGGER.info("Entering /AssignRolesToUser Mapping --> assignRolesToUser method");

		List<UserRole> persistedEntries = null;

		try {

			if (employeeId == 0 || roles.isEmpty() || null == roles)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			String[] rolesArray = roles.split(",");
			Map<Long, Boolean> presentRoles = new HashMap<Long, Boolean>();
			for (int i = 0; i < rolesArray.length; i++) {
				presentRoles.put(Long.valueOf(rolesArray[i]), false);
			}

			List<UserRole> existingMappingFromDb = null;
			existingMappingFromDb = userRoleService.findByUserId(employeeId);
			if (existingMappingFromDb.size() != 0) {
				for (UserRole currentUserRole : existingMappingFromDb) {
					currentUserRole.setActive(Constants.INACTIVE);
				}
			}

			for (int i = 0; i < rolesArray.length; i++) {

				for (int j = 0; j < existingMappingFromDb.size(); j++) {

					if (Long.valueOf(rolesArray[i]).equals(existingMappingFromDb.get(j).getRole().getId())) {
						existingMappingFromDb.get(j).setActive(Constants.ACTIVE);
						existingMappingFromDb.get(j).getRole().setId(Long.valueOf(rolesArray[i]));
						existingMappingFromDb.get(j).getUser().setId(employeeId);
						presentRoles.put(existingMappingFromDb.get(j).getRole().getId(), true);
					}
				}

			}
			// userRoleService.saveUserRoleMappings(existingMappingFromDb);

			for (Entry<Long, Boolean> currentEntry : presentRoles.entrySet()) {
				if (!currentEntry.getValue()) {
					Role role = new Role();
					role.setId(currentEntry.getKey());
					User user = new User();
					user.setId(employeeId);
					UserRole userRoleObj = new UserRole();
					userRoleObj.setRole(role);
					userRoleObj.setUser(user);
					if (null != existingMappingFromDb) {
						existingMappingFromDb.add(userRoleObj);
					} else {
						existingMappingFromDb = new ArrayList<UserRole>();
						existingMappingFromDb.add(userRoleObj);
					}
				}
			}
			persistedEntries = userRoleService.saveUserRoleMappings(existingMappingFromDb);
		} catch (ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw new Exception(e);
		}

		LOGGER.info("Exiting /AssignRolesToUser Mapping --> assignRolesToUser method");
		return persistedEntries;

	}

	// Operation Related - Start
	@PostMapping(value = "/registerOperation/{operationToPersist}", produces = "application/json")
	public Operations registerNewOperation(@PathVariable String operationToPersist) throws Exception {

		LOGGER.info("Entering /registerOperation Mapping --> registerNewOperation method");
		Operations operation = null;

		try {
			if (operationToPersist == null || operationToPersist.isEmpty())
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			operation = new Operations();
			operation.setOperationName(operationToPersist);
			operation.setActive(Constants.ACTIVE);
			operation = operationsService.saveOperations(operation);

		} catch (OperationAlreadyPresentException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting /registerOperation Mapping --> registerNewOperation method");
		return operation;
	}

	@PostMapping(value = "/updateOperation/{operationId}/{operationToPersist}", produces = "application/json")
	public Operations updateExistingOperation(@PathVariable String operationToPersist, @PathVariable long operationId)
			throws Exception {

		LOGGER.info("Entering /updateOperation Mapping --> updateExistingOperation method");

		Operations operation = null;

		try {
			if (operationToPersist == null || operationToPersist.isEmpty() || operationId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			operation = operationsService.findById(operationId);

			operation.setId(operationId);
			operation.setOperationName(operationToPersist);
			// operation.setActive(Constants.ACTIVE);
			operation = operationsService.saveOperations(operation);
		} catch (NoSuchOperationFound | ImproperParametersPassedException | OperationAlreadyPresentException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting /updateOperation Mapping --> updateExistingOperation method");
		return operation;
	}

	@DeleteMapping(value = "/deleteOperation/{operationId}", produces = "application/json")
	public ResponseEntity<Operations> deleteExistingOperation(@PathVariable long operationId) throws Exception {

		LOGGER.info("Entering /deleteOperation Mapping --> deleteExistingOperation method");

		Operations operationToDelete = null;

		try {
			if (operationId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			operationToDelete = operationsService.deleteOperation(operationId);
		} catch (ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Entering /deleteGroup Mapping --> deleteGroup method");
		return ResponseEntity.ok(operationToDelete);

	}

	@DeleteMapping(value = "/reactivateOperation/{operationId}", produces = "application/json")
	public ResponseEntity<Operations> reactivateOperation(@PathVariable long operationId) throws Exception {

		LOGGER.info("Entering /reactivateOperation Mapping --> reactivateOperation method");

		Operations operationToDelete = null;

		try {
			if (operationId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			operationToDelete = operationsService.reactivateOperation(operationId);
		} catch (ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Entering /reactivateOperation Mapping --> reactivateOperation method");
		return ResponseEntity.ok(operationToDelete);

	}

	// Operation Related - End
	@PostMapping(value = "/AssignOperationsForRole/{roleId}/{operationIds}", produces = "application/json")
	public List<RoleOperations> assignOperationsToRole(@PathVariable long roleId, @PathVariable String operationIds)
			throws Exception {

		LOGGER.info("Entering /AssignOperationsForRole Mapping --> assignOperationsToRole method");

		List<RoleOperations> persistedEntries = null;

		try {

			if (roleId == 0 || operationIds.isEmpty() || null == operationIds)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			Map<Long, Boolean> presentOperations = new HashMap<Long, Boolean>();
			String[] operationsArry = operationIds.split(",");
			for (int i = 0; i < operationsArry.length; i++) {
				presentOperations.put(Long.valueOf(operationsArry[i]), false);
			}

			List<RoleOperations> existingMappingFromDb = null;
			existingMappingFromDb = roleOperationsService.findByRoleId(roleId);
			if (existingMappingFromDb.size() != 0) {
				for (RoleOperations currentOperation : existingMappingFromDb) {
					currentOperation.setActive(Constants.INACTIVE);
				}
			}

			for (int i = 0; i < operationsArry.length; i++) {

				for (int j = 0; j < existingMappingFromDb.size(); j++) {

					if (Long.valueOf(operationsArry[i]).equals(existingMappingFromDb.get(j).getOperation().getId())) {
						existingMappingFromDb.get(j).setActive(Constants.ACTIVE);
						presentOperations.put(existingMappingFromDb.get(j).getOperation().getId(), true);
					}
				}

			}
			// userRoleService.saveUserRoleMappings(existingMappingFromDb);

			for (Entry<Long, Boolean> currentEntry : presentOperations.entrySet()) {
				if (!currentEntry.getValue()) {
					Role role = new Role();
					role.setId(roleId);
					Operations operations = new Operations();
					operations.setId(currentEntry.getKey());
					RoleOperations roleOps = new RoleOperations();
					roleOps.setOperation(operations);
					roleOps.setRole(role);
					roleOps.setActive(Constants.ACTIVE);
					if (null == existingMappingFromDb)
						existingMappingFromDb = new ArrayList<RoleOperations>();

					existingMappingFromDb.add(roleOps);
				}
			}
			persistedEntries = roleOperationsService.saveRoleOperationsMapping(existingMappingFromDb);
		} catch (ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw new Exception(e);
		}

		LOGGER.info("Exiting /AssignOperationsForRole Mapping --> assignOperationsToRole method");
		return persistedEntries;

	}

	@GetMapping(value = "/fetchUsers/{activeStatus}")
	public List<User> findUsers(@PathVariable String activeStatus) {
		LOGGER.info("Entering /fetchUsers Mapping --> findUsers method");

		List<User> usersList = userService.findAllByActive(activeStatus);

		LOGGER.info("Entering /fetchUsers Mapping --> findUsers method");
		return usersList;

	}

	@GetMapping(value = "/fetchRoles/{activeStatus}")
	public List<Role> findRoles(@PathVariable String activeStatus) {
		LOGGER.info("Entering /fetchRoles Mapping --> findRoles method");

		List<Role> rolesList = roleService.findAllByActive(activeStatus);

		LOGGER.info("Exiting /fetchRoles Mapping --> findRoles method");
		return rolesList;

	}

	@GetMapping(value = "/fetchUserRoles/{activeStatus}")
	public List<UserRole> findUserRoles(@PathVariable String activeStatus) {
		LOGGER.info("Entering /fetchUserRoles Mapping --> findUserRoles method");

		List<UserRole> rolesList = userRoleService.findAllByActive(activeStatus);

		LOGGER.info("Entering /fetchUserRoles Mapping --> findUserRoles method");
		return rolesList;

	}

	@GetMapping(value = "/fetchOperations/{activeStatus}")
	public List<Operations> findOperations(@PathVariable String activeStatus) {
		LOGGER.info("Entering /fetchOperations Mapping --> findOperations method");

		List<Operations> operationList = operationsService.findAllByActive(activeStatus);

		LOGGER.info("Entering /fetchOperations Mapping --> findOperations method");
		return operationList;

	}

	@GetMapping(value = "/fetchRoleOperations/{activeStatus}")
	public List<RoleOperations> findRoleOperations(@PathVariable String activeStatus) {
		LOGGER.info("Entering /fetchRoleOperations Mapping --> findRoleOperations method");
		List<RoleOperations> roleOperationsList = roleOperationsService.findAllByActive(activeStatus);

		LOGGER.info("Exiting /fetchRoleOperations Mapping --> findRoleOperations method");
		return roleOperationsList;

	}

	// Group Related - Start
	@PostMapping(value = "/registerGroup/{groupNameToPersist}", produces = "application/json")
	public Group registerNewGroup(@PathVariable String groupNameToPersist) throws Exception {

		LOGGER.info("Entering /registerGroup Mapping --> registerNewGroup method");

		Group groupToPersist = null;

		try {

			if (null == groupNameToPersist || groupNameToPersist.isEmpty())
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			groupToPersist = new Group();
			groupToPersist.setActive(Constants.ACTIVE);
			groupToPersist.setGroupName(groupNameToPersist);
			groupToPersist = groupService.saveGroup(groupToPersist);

		} catch (GroupAlreadyPresentException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Existing /registerGroup Mapping --> registerNewGroup method");
		return groupToPersist;
	}

	@PostMapping(value = "/updateGroup/{groupId}/{groupNameToPersist}", produces = "application/json")
	public Group updateExistingGroup(@PathVariable long groupId, @PathVariable String groupNameToPersist)
			throws Exception {

		LOGGER.info("Entering /updateGroup Mapping --> updateExistingGroup method");

		Group groupToUpdate = null;

		try {

			if (null == groupNameToPersist || groupNameToPersist.isEmpty() || groupId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			groupToUpdate = new Group();
			groupToUpdate.setId(groupId);
			groupToUpdate.setGroupName(groupNameToPersist);
			groupToUpdate.setActive(Constants.ACTIVE);
			groupToUpdate = groupService.updateGroup(groupToUpdate);
		} catch (NoSuchGroupFound | GroupAlreadyPresentException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting /updateGroup Mapping --> updateExistingGroup method");
		return groupToUpdate;
	}

	@DeleteMapping(value = "/deleteGroup/{groupId}", produces = "application/json")
	public ResponseEntity<Group> deleteExistingGroup(@PathVariable long groupId) throws Exception {

		LOGGER.info("Entering /deleteGroup Mapping --> deleteGroup method");

		Group groupToDelete = null;

		try {
			if (groupId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			groupToDelete = groupService.deleteExistingGroup(groupId);
		} catch (NoSuchGroupFound | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Entering /deleteGroup Mapping --> deleteGroup method");
		return ResponseEntity.ok(groupToDelete);

	}

	@PostMapping(value = "/reactivateGroup/{groupId}", produces = "application/json")
	public ResponseEntity<Group> reactivateExistingGroup(@PathVariable long groupId) throws Exception {

		LOGGER.info("Entering /reactivateExistingGroup Mapping --> deleteGroup method");

		Group groupToReactivate = null;

		try {
			if (groupId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			groupToReactivate = groupService.reactivateExistingGroup(groupId);
		} catch (NoSuchGroupFound | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Entering /reactivateExistingGroup Mapping --> deleteGroup method");
		return ResponseEntity.ok(groupToReactivate);

	}

	// Group Related - End

	// RoleGroup - Start
	@PostMapping(value = "/assignRolesForGroup/{groupId}/{rolesForGroup}", produces = "application/json")
	public List<RoleGroup> registerRolesForGroup(@PathVariable long groupId, @PathVariable String rolesForGroup)
			throws Exception {

		LOGGER.info("Entering /assignRolesForGroup Mapping --> registerRolesForGroup method");

		Group group = null;
		List<RoleGroup> existingRoleGroupMapping = null;
		Map<Long, Boolean> presentRoles = new HashMap<Long, Boolean>();
		List<RoleGroup> roleGroupPersisted = null;

		try {
			if (groupId == 0 || rolesForGroup.isEmpty() || null == rolesForGroup)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			group = groupService.findById(groupId);

			String[] rolesArray = rolesForGroup.split(",");

			for (int i = 0; i < rolesArray.length; i++) {
				presentRoles.put(Long.valueOf(rolesArray[i]), false);
			}

			existingRoleGroupMapping = roleGroupService.findByGroupId(groupId);
			if (existingRoleGroupMapping.size() > 0) {
				existingRoleGroupMapping.stream().forEach(item -> item.setActive(Constants.INACTIVE));

				for (int i = 0; i < rolesArray.length; i++) {
					for (int j = 0; j < existingRoleGroupMapping.size(); j++) {

						if (Long.valueOf(rolesArray[i]).equals(existingRoleGroupMapping.get(j).getRole().getId())) {
							existingRoleGroupMapping.get(j).setActive(Constants.ACTIVE);
							presentRoles.put(existingRoleGroupMapping.get(j).getRole().getId(), true);
						}
					}

				}
			}

			for (Entry<Long, Boolean> currentEntry : presentRoles.entrySet()) {
				if (!currentEntry.getValue()) {

					Role currentRole = new Role();
					currentRole.setId(Long.valueOf(currentEntry.getKey()));

					RoleGroup roleGroupMapping = new RoleGroup();
					roleGroupMapping.setRole(currentRole);
					roleGroupMapping.setGroup(group);
					roleGroupMapping.setActive(Constants.ACTIVE);

					if (null == existingRoleGroupMapping)
						existingRoleGroupMapping = new ArrayList<RoleGroup>();
					existingRoleGroupMapping.add(roleGroupMapping);
				}
			}

			roleGroupPersisted = roleGroupService.saveAllRoleGroupEntity(existingRoleGroupMapping);

			List<UserGroup> existingUsersForGroup = userGroupService.findByGroupId(groupId);
			List<UserRole> userRolesUpdatedList = null;

			for (int i = 0; i < existingUsersForGroup.size(); i++) {

				for (int j = 0; j < rolesArray.length; j++) {

					User user = new User();
					user.setId(existingUsersForGroup.get(i).getUser().getId());

					Role role = new Role();
					role.setId(Long.valueOf(rolesArray[j]));

					UserRole userRole = new UserRole();
					userRole.setRole(role);
					userRole.setUser(user);
					userRole.setActive(Constants.ACTIVE);

					if ((userRolesUpdatedList == null))
						userRolesUpdatedList = new ArrayList<UserRole>();
					userRolesUpdatedList.add(userRole);

				}

			}
			userRoleService.saveUserRoleMappings(userRolesUpdatedList);

		} catch (ImproperParametersPassedException | NoSuchGroupFound e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Entering /assignRolesForGroup Mapping --> registerRolesForGroup method");
		return roleGroupPersisted;

	}
	// Role Group - End

	// User Group - Start
	@PostMapping(value = "/assignUsersForGroup/{groupId}/{userIdForGroup}", produces = "application/json")
	public List<UserGroup> registerUserForGroups(@PathVariable long groupId, @PathVariable String userIdForGroup)
			throws Exception {

		LOGGER.info("Entering /assignUsersForGroup Mapping --> registerUserForGroups method");
		Group group = null;
		List<UserGroup> existingUserGroupMapping = null;
		Map<Long, Boolean> presentUsers = new HashMap<Long, Boolean>();
		List<UserGroup> userGroupPersisted = null;
		List<Long> roleIds = new ArrayList<Long>();

		try {
			if (groupId == 0 || userIdForGroup.isEmpty() || null == userIdForGroup)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			group = groupService.findById(groupId);

			String[] userArray = userIdForGroup.split(",");

			for (int i = 0; i < userArray.length; i++) {
				presentUsers.put(Long.valueOf(userArray[i]), false);
			}

			existingUserGroupMapping = userGroupService.findByGroupId(groupId);
			if (existingUserGroupMapping.size() > 0) {
				existingUserGroupMapping.stream().forEach(item -> item.setActive(Constants.INACTIVE));

				for (int i = 0; i < userArray.length; i++) {
					for (int j = 0; j < existingUserGroupMapping.size(); j++) {

						if (Long.valueOf(userArray[i]).equals(existingUserGroupMapping.get(j).getUser().getId())) {
							existingUserGroupMapping.get(j).setActive(Constants.ACTIVE);
							presentUsers.put(existingUserGroupMapping.get(j).getUser().getId(), true);
						}
					}

				}
			}

			for (Entry<Long, Boolean> currentEntry : presentUsers.entrySet()) {
				if (!currentEntry.getValue()) {

					User user = new User();
					user.setId(Long.valueOf(currentEntry.getKey()));

					UserGroup userGroupMapping = new UserGroup();
					userGroupMapping.setGroup(group);
					userGroupMapping.setUsers(user);
					userGroupMapping.setActive(Constants.ACTIVE);

					if (null == existingUserGroupMapping)
						existingUserGroupMapping = new ArrayList<UserGroup>();
					existingUserGroupMapping.add(userGroupMapping);
				}
			}

			userGroupPersisted = userGroupService.saveAllUserGroup(existingUserGroupMapping);

			List<RoleGroup> roleGroupMappingFromDb = roleGroupService.findByGroupId(groupId);
			List<UserRole> updatedUserRole = null;
			List<UserRole> updatedUserRoleFromDb = null;

			roleGroupMappingFromDb.stream().forEach(item -> roleIds.add(item.getRole().getId()));

			updatedUserRoleFromDb = userRoleService.findByRoleIdInAndUserIdIn(roleIds,
					Stream.of(userArray).map(Long::valueOf).collect(Collectors.toList()));

			for (int i = 0; i < roleGroupMappingFromDb.size(); i++) {

				for (int j = 0; j < userArray.length; j++) {

					UserRole userRole = new UserRole();

					User user = new User();
					user.setId(Long.valueOf(userArray[j]));

					Role role = new Role();
					role.setId(roleGroupMappingFromDb.get(i).getRole().getId());

					if (null == updatedUserRole)
						updatedUserRole = new ArrayList<UserRole>();

					userRole.setRole(role);
					userRole.setUser(user);
					userRole.setActive(Constants.ACTIVE);

					boolean isPresent = checkUserRoleExistence(userRole, updatedUserRoleFromDb);
					if (!isPresent)
						updatedUserRole.add(userRole);

				}
			}

			if (null != updatedUserRole && updatedUserRole.size() > 0)
				userRoleService.saveUserRoleMappings(updatedUserRole);

		} catch (ImproperParametersPassedException | NoSuchGroupFound e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;

		} catch (Exception e) {

			LOGGER.error(Constants.BLANK, e);
			throw e;

		}

		LOGGER.info("Exiting /assignUsersForGroup Mapping --> registerUserForGroups method");
		return userGroupPersisted;

	}
	// User Group - End

	// Entity - Type - Start
	@PostMapping(value = "/registerEntityType/{entityTypeName}/{entityTypeDescription}", produces = "application/json")
	public EntitiesType registerNewEntityType(@PathVariable String entityTypeName,
			@PathVariable String entityTypeDescription) throws Exception {

		LOGGER.info("Entering /registerEntityType Mapping --> registerNewEntityType method");
		EntitiesType persistedEntityType = null;
		try {
			if ((entityTypeName.isEmpty() || null == entityTypeName)
					|| (entityTypeDescription.isEmpty() || null == entityTypeDescription)) {
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			}
			EntitiesType entitiesType = new EntitiesType();
			entitiesType.setName(entityTypeName);
			entitiesType.setDescription(entityTypeDescription);
			entitiesType.setActive(Constants.ACTIVE);
			persistedEntityType = entityTypeService.saveEntitiesType(entitiesType);
		} catch (EntityTypeAlreadyPresentException | ImproperParametersPassedException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting /registerEntityType Mapping --> registerNewEntityType method");
		return persistedEntityType;
	}

	@PostMapping(value = "/updateEntityType/{entityTypeId}/{entityTypeName}/{entityTypeDescription}", produces = "application/json")
	public EntitiesType updateExistingEntityType(@PathVariable long entityTypeId, @PathVariable String entityTypeName,
			@PathVariable String entityTypeDescription) throws Exception {

		LOGGER.info("Entering /updateEntityType Mapping --> updateExistingEntityType method");

		EntitiesType entitiesType = null;

		try {

			if ((null == entityTypeName || entityTypeName.isEmpty()) || entityTypeId == 0
					|| (null == entityTypeDescription || entityTypeDescription.isEmpty()))
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			entitiesType = entityTypeService.findById(entityTypeId);
			entitiesType.setId(entityTypeId);
			entitiesType.setName(entityTypeName);
			entitiesType.setDescription(entityTypeDescription);
			entitiesType.setActive(Constants.ACTIVE);
			entitiesType = entityTypeService.saveEntitiesType(entitiesType);
		} catch (EntityTypeAlreadyPresentException | ImproperParametersPassedException | NoSuchEntityTypeFound e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}
		LOGGER.info("Exiting /updateEntityType Mapping --> updateExistingEntityType method");
		return entitiesType;
	}

	@DeleteMapping(value = "/deleteEntityType/{entityTypeId}", produces = "application/json")
	public EntitiesType deleteExistingEntityType(@PathVariable long entityTypeId)
			throws ImproperParametersPassedException, NoSuchEntityTypeFound {
		LOGGER.info("Entering /deleteEntityType Mapping --> deleteExistingEntityType method");

		if (entityTypeId == 0)
			throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

		EntitiesType persistedEntityType = null;

		try {

			persistedEntityType = entityTypeService.findById(entityTypeId);

			persistedEntityType.setId(entityTypeId);
			persistedEntityType.setActive(Constants.INACTIVE);
			persistedEntityType = entityTypeService.activeOrInactivePersist(persistedEntityType);
		} catch (NoSuchEntityTypeFound e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}
		LOGGER.info("Exiting /deleteEntityType Mapping --> deleteExistingEntityType method");
		return persistedEntityType;

	}

	@PostMapping(value = "/reactivateEntityType/{entityTypeId}", produces = "application/json")
	public EntitiesType reactivateExistingEntityType(@PathVariable long entityTypeId)
			throws ImproperParametersPassedException, NoSuchEntityTypeFound {
		LOGGER.info("Entering /deleteEntityType Mapping --> deleteExistingEntityType method");

		if (entityTypeId == 0)
			throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

		EntitiesType persistedEntityType = null;

		try {

			persistedEntityType = entityTypeService.findById(entityTypeId);
			persistedEntityType.setId(entityTypeId);
			persistedEntityType.setActive(Constants.ACTIVE);
			persistedEntityType = entityTypeService.activeOrInactivePersist(persistedEntityType);

		} catch (NoSuchEntityTypeFound e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}
		LOGGER.info("Exiting /deleteEntityType Mapping --> deleteExistingEntityType method");
		return persistedEntityType;
	}

	@PostMapping(value = "/registerEntityAttributeDefinition/{entityTypeId}/{attributeId}", produces = "application/json")
	public List<EntityAttributeDefinition> registerEntityAttributeDefinition(@PathVariable long entityTypeId,
			@PathVariable String attributeId) {

		LOGGER.info("Entering /registerEntityAttributeDefinition Mapping --> registerEntityAttributeDefinition method");

		List<EntityAttributeDefinition> entAttribDefinitionlist = null;
		Map<Long, Boolean> existingAttributeList = new HashMap<Long, Boolean>();
		String[] attributeIdArray = attributeId.split(",");
		for (int i = 0; i < attributeIdArray.length; i++) {
			existingAttributeList.put(Long.valueOf(attributeIdArray[i]), false);

		}

		entAttribDefinitionlist = entityAttributeDefinitionService.findByEntityTypeId(entityTypeId);

		entAttribDefinitionlist.stream().forEach(item -> item.setActive(Constants.INACTIVE));

		for (int i = 0; i < attributeIdArray.length; i++) {

			for (int j = 0; j < entAttribDefinitionlist.size(); j++) {
				if (entAttribDefinitionlist.get(j).getEntityAttributeValues().getId() == Long
						.valueOf(attributeIdArray[i])) {
					entAttribDefinitionlist.get(j).setActive(Constants.ACTIVE);
					existingAttributeList.put(Long.valueOf(attributeIdArray[i]), true);
				}
			}

		}

		for (Entry<Long, Boolean> currentEntrySet : existingAttributeList.entrySet()) {
			if (!currentEntrySet.getValue()) {
				EntitiesAttribute etAttribute = new EntitiesAttribute();
				etAttribute.setId(currentEntrySet.getKey());

				EntitiesType entityType = new EntitiesType();
				entityType.setId(entityTypeId);

				if (entAttribDefinitionlist == null)
					entAttribDefinitionlist = new ArrayList<EntityAttributeDefinition>();

				EntityAttributeDefinition entityAttribDefinition = new EntityAttributeDefinition();
				entityAttribDefinition.setEntityAttributeValues(etAttribute);
				entityAttribDefinition.setEntityType(entityType);
				entityAttribDefinition.setActive(Constants.ACTIVE);

				entAttribDefinitionlist.add(entityAttribDefinition);
			}
		}

		List<EntityAttributeDefinition> persistedEntries = entityAttributeDefinitionService
				.saveAllEntitiesAttribute(entAttribDefinitionlist);

		LOGGER.info("Exiting /registerEntityAttributeDefinition Mapping --> registerEntityAttributeDefinition method");
		return persistedEntries;
	}

	@GetMapping(value = "/fetchGroupForUser/{userId}", produces = "application/json")
	public List<Group> getGroupForUser(@PathVariable long userId) {
		LOGGER.info("Entering /fetchGroupForUser Mapping --> getGroupForUser method");

		List<Group> groupsForUser = userGroupService.findGroupsForUser(userId);

		LOGGER.info("Exiting /fetchGroupForUser Mapping --> getGroupForUser method");
		return groupsForUser;

	}

	@GetMapping(value = "/fetchUsersForGroup/{groupId}", produces = "application/json")
	public List<User> getUsersForGroup(@PathVariable long groupId) {
		LOGGER.info("Entering /fetchUsersForGroup Mapping --> getUsersForGroup method");

		List<User> usersForGroup = userGroupService.findUsersForGroup(groupId);

		LOGGER.info("Entering /fetchUsersForGroup Mapping --> getUsersForGroup method");
		return usersForGroup;

	}

	@GetMapping(value = "/fetchGroupsForRole/{roleId}", produces = "application/json")
	public List<Group> getGroupsForRole(@PathVariable long roleId) {
		LOGGER.info("Entering /fetchGroupsForRole Mapping --> getGroupsForRole method");

		List<Group> assignedGroupForRole = roleGroupService.findGroupsByRole(roleId);

		LOGGER.info("Entering /fetchGroupsForRole Mapping --> getGroupsForRole method");
		return assignedGroupForRole;
	}

	@GetMapping(value = "/fetchRolesForGroup/{groupId}", produces = "application/json")
	public List<Role> getRolesForGroup(@PathVariable long groupId) {
		LOGGER.info("Entering /registerUser Mapping --> registerNewUserJson method");
		List<Role> rolesAssignedForGroup = roleGroupService.findRolesByGroup(groupId);
		return rolesAssignedForGroup;
	}

	@GetMapping(value = "/fetchRolesForUser/{userId}", produces = "application/json")
	public List<Role> getRolesForAUser(@PathVariable long userId) {
		LOGGER.info("Entering /fetchRolesForUser Mapping --> getRolesForAUser method");

		List<Role> rolesForUser = userRoleService.findByUserAssigned(userId);

		LOGGER.info("Exiting /fetchRolesForUser Mapping --> getRolesForAUser method");
		return rolesForUser;
	}

	@GetMapping(value = "/fetchUsersForRole/{roleId}", produces = "application/json")
	public List<User> getUsersForRole(@PathVariable long roleId) {
		LOGGER.info("Entering /fetchUsersForRole/ Mapping --> getUsersForRole method");

		List<User> userAssignedToRole = userRoleService.findByRoleAssigned(roleId);

		LOGGER.info("Exiting /fetchUsersForRole/ Mapping --> getUsersForRole method");
		return userAssignedToRole;
	}

	/*
	 * // Entities and entity Type
	 * 
	 * @GetMapping(value = "/fetchEntitiesTypeForUser/{userId}", produces =
	 * "application/json") public List<EntitiesType>
	 * getEntitiesTypeForUser(@PathVariable long userId) { LOGGER.
	 * info("Entering /fetchEntitiesTypeForUser Mapping --> getEntitiesTypeForUser method"
	 * );
	 * 
	 * List<EntitiesType> entitiesTypeForUser =
	 * entityService.findEntityTypeByUserAssigned(userId);
	 * 
	 * LOGGER.
	 * info("Exiting /fetchEntitiesTypeForUser Mapping --> getEntitiesTypeForUser method"
	 * ); return entitiesTypeForUser; }
	 */

	// Entities and entity Type

	@PostMapping("/addEntityTypeAttributeDefinition/{entityTypeId}/{attributeName}/{attributeDescription}/{attributeValue}")
	public ResponseEntity<EntityTypeAttributeDefinition> addEntityTypeAttributeDefinition(
			@PathVariable long entityTypeId, @PathVariable String attributeName,
			@PathVariable String attributeDescription, @PathVariable String attributeValue) throws Exception {

		LOGGER.info("Entering addEntityTypeAttributeDefinition Method");

		EntityTypeAttributeDefinition entityTypeDefinition = null;
		try {

			if (null == attributeName || attributeName.isEmpty() || entityTypeId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			entityTypeDefinition = new EntityTypeAttributeDefinition();

			EntitiesType entityType = new EntitiesType();
			entityType.setId(entityTypeId);

			entityTypeDefinition.setEntityType(entityType);
			entityTypeDefinition.setAttributeName(attributeName);
			entityTypeDefinition.setAttributeDescription(attributeDescription);
			entityTypeDefinition.setAttributeValue(attributeValue);
			entityTypeDefinition.setActive(Constants.ACTIVE);

			entityTypeAttributeService.saveEntityTypeAttributeDefinition(entityTypeDefinition);
		} catch (ImproperParametersPassedException | EntityTypeAttributeDefinitionAlreadyPresent e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}
		LOGGER.info("Exiting addEntityTypeAttributeDefinition Method");
		return ResponseEntity.ok(entityTypeDefinition);

	}

	@PostMapping("/updateEntityTypeAttributeDefinition/{entityTypeAttributeDefinitionId}/{entityTypeId}/{attributeName}/{attributeDescription}/{attributeValue}")
	public ResponseEntity<EntityTypeAttributeDefinition> updateExistingEntityTypeAttributeDefinition(
			@PathVariable long entityTypeAttributeDefinitionId, @PathVariable long entityTypeId,
			@PathVariable String attributeName, @PathVariable String attributeDescription,
			@PathVariable String attributeValue) throws Exception {

		LOGGER.info("Entering updateEntityTypeAttributeDefinition Method");

		EntityTypeAttributeDefinition entityTypeDefinition = null;
		try {

			if (null == attributeName || attributeName.isEmpty() || entityTypeId == 0
					|| entityTypeAttributeDefinitionId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			entityTypeDefinition = entityTypeAttributeService.findById(entityTypeAttributeDefinitionId);

			EntitiesType entityType = new EntitiesType();
			entityType.setId(entityTypeId);

			entityTypeDefinition.setId(entityTypeAttributeDefinitionId);
			entityTypeDefinition.setEntityType(entityType);
			entityTypeDefinition.setAttributeName(attributeName);
			entityTypeDefinition.setAttributeDescription(attributeDescription);
			entityTypeDefinition.setAttributeValue(attributeValue);
			entityTypeDefinition.setActive(Constants.ACTIVE);

			entityTypeDefinition = entityTypeAttributeService.saveEntityTypeAttributeDefinition(entityTypeDefinition);
		} catch (ImproperParametersPassedException | EntityTypeAttributeDefinitionAlreadyPresent e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting updateEntityTypeAttributeDefinition Method");
		return ResponseEntity.ok(entityTypeDefinition);

	}

	@DeleteMapping("/deleteEntityTypeAttributeDefinition/{entityTypeAttributeId}")
	public ResponseEntity<EntityTypeAttributeDefinition> deleteEntityTypeAttributeDefinition(
			@PathVariable long entityTypeAttributeId) {

		LOGGER.info("Entering deleteEntityTypeAttributeDefinition Method");

		EntityTypeAttributeDefinition existingDbEntries = null;
		try {
			existingDbEntries = entityTypeAttributeService.findById(entityTypeAttributeId);
			if (null == existingDbEntries)
				throw new NoSuchEntityAttributeFoundException(Constants.NO_ENTITY_TYPE_ATTRIBUTE_DEFINITION_FOUND);
			else {
				existingDbEntries.setActive(Constants.INACTIVE);
				entityTypeAttributeService.activateOrReactivateEntityTypeAttributeDefinition(existingDbEntries);
			}

		} catch (NoSuchEntityTypeAttributeDefinitionFound | NoSuchEntityAttributeFoundException e) {
			LOGGER.error(Constants.BLANK, e);
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting deleteEntityTypeAttributeDefinition Method");
		return ResponseEntity.ok(existingDbEntries);

	}

	@PostMapping("/reactivateEntityTypeAttributeDefinition/{entityTypeAttributeId}")
	public ResponseEntity<EntityTypeAttributeDefinition> reActivateEntityTypeAttributeDefinition(
			@PathVariable long entityTypeAttributeId) throws Exception {

		LOGGER.info("Entering reActivateEntityTypeAttributeDefinition Method");

		EntityTypeAttributeDefinition existingDbEntries = null;
		try {
			existingDbEntries = entityTypeAttributeService.findById(entityTypeAttributeId);
			if (null == existingDbEntries)
				throw new NoSuchEntityAttributeFoundException(Constants.EMPLOYEE_ID_PRESENT);
			else
				existingDbEntries.setActive(Constants.ACTIVE);

			entityTypeAttributeService.activateOrReactivateEntityTypeAttributeDefinition(existingDbEntries);
		} catch (NoSuchEntityTypeAttributeDefinitionFound | NoSuchEntityAttributeFoundException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting reActivateEntityTypeAttributeDefinition Method");
		return ResponseEntity.ok(existingDbEntries);

	}

	@PostMapping("/addEntity/{entityTypeId}/{entityName}/{entityDescription}/{entityDisplayName}")
	public ResponseEntity<Entities> registerNewEntity(@PathVariable long entityTypeId, @PathVariable String entityName,
			@PathVariable String entityDescription, @PathVariable String entityDisplayName) throws Exception {

		LOGGER.info("Entering registerNewEntity Method");

		Entities entity = null;
		try {

			if (entityTypeId == 0 || null == entityName || entityName.isEmpty())
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			entity = new Entities();

			EntitiesType entityType = new EntitiesType();
			entityType.setId(entityTypeId);

			entity.setEntityType(entityType);
			entity.setActive(Constants.ACTIVE);
			entity.setDescription(entityDescription);
			entity.setEntityName(entityName);
			entity.setDisplayName(entityDisplayName);
			entityService.saveEntities(entity);
		} catch (EntityAlreadyPresentException | ImproperParametersPassedException e) {
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting registerNewEntity Method");
		return ResponseEntity.ok(entity);

	}

	@PostMapping("/updateEntity/{entityId}/{entityTypeId}/{entityName}/{entityDescription}/{entityDisplayName}")
	public ResponseEntity<Entities> updateExistingEntity(@PathVariable long entityId, @PathVariable long entityTypeId,
			@PathVariable String entityName, @PathVariable String entityDescription,
			@PathVariable String entityDisplayName) throws Exception {

		LOGGER.info("Entering updateExistingEntity Method");
		Entities entity = null;

		try {

			entity = entityService.findbyId(entityTypeId);
			EntitiesType entityType = new EntitiesType();
			entityType.setId(entityTypeId);

			entity.setId(entityId);
			entity.setEntityType(entityType);
			entity.setActive(Constants.ACTIVE);
			entity.setDescription(entityDescription);
			entity.setEntityName(entityName);
			entity.setDisplayName(entityDisplayName);
			entity = entityService.updateEntity(entity);
		} catch (ImproperParametersPassedException | NoSuchEntityFound | EntityAlreadyPresentException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting updateExistingEntity Method");
		return ResponseEntity.ok(entity);

	}

	@DeleteMapping("/deleteEntity/{entityId}")
	public ResponseEntity<Entities> deleteExistingEntity(@PathVariable long entityId) throws NoSuchEntityFound {

		LOGGER.info("Entering updateExistingEntity Method");
		Entities entity = null;

		try {
			entityService.deleteExistingEntity(entityId);
		} catch (NoSuchEntityFound e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting updateExistingEntity Method");
		return ResponseEntity.ok(entity);

	}

	@PostMapping("/reactivateEntity/{entityId}")
	public ResponseEntity<Entities> reactivateDeletedEntity(@PathVariable long entityId) throws NoSuchEntityFound {

		LOGGER.info("Entering updateExistingEntity Method");
		Entities entity = null;

		try {
			entityService.deleteExistingEntity(entityId);
		} catch (NoSuchEntityFound e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting updateExistingEntity Method");
		return ResponseEntity.ok(entity);

	}

	@PostMapping("/addEntityAttributeValues/{entityId}/{entityTypeId}/{attributeDefinitionId}")
	public ResponseEntity<EntityAttributeValues> registerNewEntityAttributeValues(@PathVariable long entityId,
			@PathVariable long entityTypeId, @PathVariable long attributeDefinitionId) throws Exception {

		LOGGER.info("Entering registerNewEntityAttributeValues Method");

		EntityAttributeValues attributeValue = null;

		try {
			if (entityId == 0 || entityTypeId == 0 || attributeDefinitionId == 0)
				throw new ImproperParametersPassedException("");
			else {
				attributeValue = new EntityAttributeValues();

				EntitiesType entityType = new EntitiesType();
				entityType.setId(entityTypeId);

				EntityTypeAttributeDefinition entityAttributeDefinition = new EntityTypeAttributeDefinition();
				entityAttributeDefinition.setId(attributeDefinitionId);

				Entities entity = new Entities();
				entity.setId(entityId);

				attributeValue.setEntity(entity);
				attributeValue.setEntityType(entityType);
				attributeValue.setEntityAttributeDefinition(entityAttributeDefinition);
				attributeValue.setActive(Constants.ACTIVE);
				entityAttributeValueService.saveEntitiesAttribute(attributeValue);
			}
		} catch (ImproperParametersPassedException | EntityAttributeAlreadyPresentException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting registerNewEntityAttributeValues Method");
		return ResponseEntity.ok(attributeValue);

	}

	@PostMapping("/updateEntityAttributeValues/{entityAttributeValueId}/{entityId}/{entityTypeId}/{attributeDefinitionId}")
	public ResponseEntity<EntityAttributeValues> updateExistingEntityAttributeValues(
			@PathVariable long entityAttributeValueId, @PathVariable long entityId, @PathVariable long entityTypeId,
			@PathVariable long attributeDefinitionId) throws Exception {

		LOGGER.info("Entering updateExistingEntityAttributeValues Method");

		EntityAttributeValues attributeValue = null;

		try {
			if (entityId == 0 || entityTypeId == 0 || attributeDefinitionId == 0)
				throw new ImproperParametersPassedException("");
			else {
				attributeValue = entityAttributeValueService.findbyId(entityAttributeValueId);

				EntitiesType entityType = new EntitiesType();
				entityType.setId(entityTypeId);

				EntityTypeAttributeDefinition entityAttributeDefinition = new EntityTypeAttributeDefinition();
				entityAttributeDefinition.setId(attributeDefinitionId);

				Entities entity = new Entities();
				entity.setId(entityId);

				attributeValue.setEntity(entity);
				attributeValue.setEntityType(entityType);
				attributeValue.setEntityAttributeDefinition(entityAttributeDefinition);
				entityAttributeValueService.saveEntitiesAttribute(attributeValue);
			}
		} catch (ImproperParametersPassedException | EntityAttributeAlreadyPresentException
				| NoSuchEntityAttributeFoundException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting updateExistingEntityAttributeValues Method");
		return ResponseEntity.ok(attributeValue);

	}

	@PostMapping("/deleteEntityAttributeValues/{entityAttributeValueId}")
	public ResponseEntity<EntityAttributeValues> deleteExistingEntityAttributeValues(
			@PathVariable long entityAttributeValueId) throws Exception {

		LOGGER.info("Entering deleteExistingEntityAttributeValues Method");

		EntityAttributeValues attributeValue = null;

		try {
			attributeValue = entityAttributeValueService.findbyId(entityAttributeValueId);

			if (null == attributeValue)
				throw new NoSuchEntityAttributeFoundException(Constants.NO_ENTITY_ATTRIBUTE_VALUE_FOUND);
			attributeValue.setActive(Constants.INACTIVE);
			entityAttributeValueService.deleteOrReactivatePersistence(attributeValue);
		} catch (NoSuchEntityAttributeFoundException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		LOGGER.info("Exiting deleteExistingEntityAttributeValues Method");
		return ResponseEntity.ok(attributeValue);

	}

	@PostMapping("/reactivateEntityAttributeValues/{entityAttributeValueId}")
	public ResponseEntity<EntityAttributeValues> reactivateExistingEntityAttributeValues(
			@PathVariable long entityAttributeValueId) throws Exception {

		LOGGER.info("Entering reactivateExistingEntityAttributeValues Method");

		EntityAttributeValues attributeValue = null;

		try {

			attributeValue = entityAttributeValueService.findbyId(entityAttributeValueId);

			if (null == attributeValue)
				throw new NoSuchEntityAttributeFoundException(Constants.NO_ENTITY_ATTRIBUTE_VALUE_FOUND);
			attributeValue.setActive(Constants.ACTIVE);
			entityAttributeValueService.deleteOrReactivatePersistence(attributeValue);
		} catch (NoSuchEntityAttributeFoundException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;

		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}
		LOGGER.info("Exiting reactivateExistingEntityAttributeValues Method");
		return ResponseEntity.ok(attributeValue);

	}

	public boolean checkUserRoleExistence(UserRole userRole, List<UserRole> updatedUserRoleFromDb) {

		for (UserRole currentUserRole : updatedUserRoleFromDb) {
			if (currentUserRole.getRole().getId() == userRole.getRole().getId()
					&& currentUserRole.getUser().getId() == userRole.getUser().getId())
				return true;
		}
		return false;

	}

	@PostMapping(value = { "/addPermission/{roleId}/{entityTypeId}/{operationsId}/{entityBased}/{entityId}",
			"/addPermission/{roleId}/{entityTypeId}/{operationsId}/{entityBased}/{userAttribute}/{userCondition}/{userValues}" })
	public ResponseEntity<List<Permission>> registerPermission(@PathVariable long roleId,
			@PathVariable long entityTypeId, @PathVariable String operationsId, @PathVariable String entityBased,
			@PathVariable Optional<String> entityId, @PathVariable Optional<Long> userAttributeId,
			@PathVariable Optional<Long> userConditionId, @PathVariable Optional<Long> userValuesId) throws Exception {

		List<Permission> permissionToPersist = null;
		List<Permission> persistedEntries = null;

		String[] operationIdArray = null;
		String[] entityIdArray = null;
		List<Operations> operationsList = null;
		List<Entities> entitiesList = null;
		List<Long> idsOfOperation = null;
		List<Long> idsOfEntities = null;

		try {

			if (null == operationsId || operationsId.isEmpty() || roleId == 0 || entityTypeId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			Role role = new Role();
			role.setId(roleId);

			EntitiesType entityType = new EntitiesType();
			entityType.setId(entityTypeId);

			if (entityBased.equalsIgnoreCase(Constants.YES)) {
				if (null == entityId.get() || entityId.get().isEmpty())
					throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

				operationIdArray = operationsId.split(",");
				for (int i = 0; i < operationIdArray.length; i++) {

					long operationId = Long.valueOf(operationIdArray[i]);
					Operations operations = new Operations();
					operations.setId(operationId);

					if (null == operationsList)
						operationsList = new ArrayList<Operations>();

					operationsList.add(operations);

					if (null == idsOfOperation)
						idsOfOperation = new ArrayList<Long>();

					idsOfOperation.add(operationId);

				}

				entityIdArray = entityId.get().split(",");
				for (int i = 0; i < entityIdArray.length; i++) {

					long idOfEntity = Long.valueOf(entityIdArray[i]);
					Entities entity = new Entities();
					entity.setId(idOfEntity);

					if (null == entitiesList)
						entitiesList = new ArrayList<Entities>();

					entitiesList.add(entity);

					if (null == idsOfEntities)
						idsOfEntities = new ArrayList<Long>();

					idsOfEntities.add(idOfEntity);

				}
				for (int i = 0; i < operationsList.size(); i++) {

					for (int j = 0; j < entitiesList.size(); j++) {

						Permission permissions = new Permission();
						permissions.setRole(role);
						permissions.setEntityType(entityType);
						permissions.setOperation(operationsList.get(i));
						permissions.setEntity(entitiesList.get(j));
						permissions.setEntityBased(entityBased);
						permissions.setActive(Constants.ACTIVE);

						if (null == permissionToPersist)
							permissionToPersist = new ArrayList<Permission>();

						permissionToPersist.add(permissions);

					}
				}

				persistedEntries = permissionService.savePermissions(permissionToPersist, entityBased, roleId,
						entityTypeId);
				return ResponseEntity.ok(persistedEntries);

			} else if (entityBased.equalsIgnoreCase(Constants.NO)) {
				if (null == userAttributeId || userAttributeId.get() == 0 || null == userConditionId
						|| userConditionId.get() == 0 || null == userValuesId || userValuesId.get() == 0)
					throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

				Permission persistencePermission = new Permission();
				persistencePermission.setRole(role);
				persistencePermission.setEntityType(entityType);
				persistencePermission.setUserAttributes(userAttributeId.get());
				persistencePermission.setCondition(userConditionId.get());
				persistencePermission.setUserValues(userValuesId.get());
				persistencePermission.setActive(Constants.ACTIVE);
				persistencePermission.setEntityBased(entityBased);

				Permission persistedPermission = permissionService.savePermission(persistencePermission);
				persistedEntries = new ArrayList<Permission>();
				persistedEntries.add(persistedPermission);
				return ResponseEntity.ok(persistedEntries);

			}

		} catch (ImproperParametersPassedException | PermissionAlreadyExistsException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}
		return null;
	}

	@PostMapping(value = {
			"/updatePermission/{permissionId}/{roleId}/{entityTypeId}/{operationsId}/{entityBased}/{entityId}",
			"/updatePermission/{permissionId}/{roleId}/{entityTypeId}/{operationsId}/{entityBased}/{userAttribute}/{userCondition}/{userValues}" })
	public ResponseEntity<Permission> updatePermission(@PathVariable long permissionId, @PathVariable long roleId,
			@PathVariable long entityTypeId, @PathVariable long operationsId, @PathVariable String entityBased,
			@PathVariable Optional<Long> entityId, @PathVariable Optional<Long> userAttributeId,
			@PathVariable Optional<Long> userConditionId, @PathVariable Optional<Long> userValuesId) throws Exception {

		Permission permission = null;

		try {
			if (operationsId == 0 || roleId == 0 || entityTypeId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			permission = new Permission();
			permission.setId(permissionId);

			Role role = new Role();
			role.setId(roleId);

			EntitiesType entityType = new EntitiesType();
			entityType.setId(entityTypeId);

			Operations operation = new Operations();
			operation.setId(operationsId);

			permission.setRole(role);
			permission.setOperation(operation);
			permission.setEntityType(entityType);

			if (entityBased.equalsIgnoreCase(Constants.YES)) {
				if (!entityId.isPresent() || entityId.get() == 0)
					throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

				Entities entity = new Entities();
				entity.setId(entityId.get());

				permission.setEntity(entity);
				permission.setEntityBased(entityBased);
				permission.setActive(Constants.ACTIVE);

				Permission persistedPermission = permissionService.updatePermission(permission, entityBased);
				return ResponseEntity.ok(persistedPermission);

			} else if (entityBased.equalsIgnoreCase(Constants.NO)) {
				if (!userAttributeId.isPresent() || userAttributeId.get() == 0 || !userConditionId.isPresent()
						|| userConditionId.get() == 0 || !userValuesId.isPresent() || userValuesId.get() == 0) {
					throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);
				}

				permission.setUserAttributes(userAttributeId.get());
				permission.setCondition(userConditionId.get());
				permission.setUserValues(userValuesId.get());
				permission.setEntityBased(entityBased);
				permission.setActive(Constants.ACTIVE);

				Permission persistedPermission = permissionService.updatePermission(permission, entityBased);
				return ResponseEntity.ok(persistedPermission);

			}
		} catch (ImproperParametersPassedException | PermissionAlreadyExistsException | NoPermissionFoundException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

		return null;
	}

	@DeleteMapping("/deletePermission/{permissionId}")
	public ResponseEntity<Permission> deactivatePermission(@PathVariable long permissionId) throws Exception {

		try {

			if (permissionId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			Permission persistedPermission = permissionService.deletePermission(permissionId);
			return ResponseEntity.ok(persistedPermission);

		} catch (ImproperParametersPassedException | NoPermissionFoundException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

	}

	@PostMapping("/reactivatePermission/{permissionId}")
	public ResponseEntity<Permission> activatePermission(@PathVariable long permissionId) throws Exception {

		try {
			if (permissionId == 0)
				throw new ImproperParametersPassedException(Constants.IMPROPER_PARAMETERS_PASSED);

			Permission persistedPermission = permissionService.reactivatePermission(permissionId);
			return ResponseEntity.ok(persistedPermission);

		} catch (ImproperParametersPassedException | NoPermissionFoundException e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error(Constants.BLANK, e);
			throw e;
		}

	}

	public static void main(String[] args) {

		Translate translate = TranslateOptions.getDefaultInstance().getService();
		String text = "Hello, world!";

		Translation translation = translate.translate(text, TranslateOption.sourceLanguage("en"),
				TranslateOption.targetLanguage("ru"));
		
		System.out.printf("Text: %s%n", text);
	    System.out.printf("Translation: %s%n", translation.getTranslatedText());

	}

}
