package com.kodlamaio.hrmns.hrmns.business.concretes;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kodlamaio.hrmns.hrmns.business.abstracts.EmployerService;
import com.kodlamaio.hrmns.hrmns.business.abstracts.JobAdvertService;
import com.kodlamaio.hrmns.hrmns.business.abstracts.SystemWorkerService;
import com.kodlamaio.hrmns.hrmns.business.abstracts.UserEntityService;
import com.kodlamaio.hrmns.hrmns.business.dtos.GetListSystemWorkerDto;
import com.kodlamaio.hrmns.hrmns.business.requests.create.CreateSystemWorkerRequest;
import com.kodlamaio.hrmns.hrmns.business.requests.delete.DeleteSystemWorkerRequest;
import com.kodlamaio.hrmns.hrmns.business.requests.update.UpdateSystemWorkerRequest;
import com.kodlamaio.hrmns.hrmns.core.concretes.BusinessException;
import com.kodlamaio.hrmns.hrmns.core.mapping.ModelMapperService;
import com.kodlamaio.hrmns.hrmns.core.results.DataResult;
import com.kodlamaio.hrmns.hrmns.core.results.Result;
import com.kodlamaio.hrmns.hrmns.core.results.SuccessDataResult;
import com.kodlamaio.hrmns.hrmns.core.results.SuccessResult;
import com.kodlamaio.hrmns.hrmns.dataAccess.abstracts.SystemWorkerDao;
import com.kodlamaio.hrmns.hrmns.entities.SystemWorker;

@Service
public class SystemWorkerManager implements SystemWorkerService {

	private SystemWorkerDao systemWorkerDao;
	private ModelMapperService modelMapperService;
	private EmployerService employerService;
	private JobAdvertService jobAdvertService;
	private UserEntityService userEntityService;

	@Autowired
	public SystemWorkerManager(SystemWorkerDao systemWorkerDao, ModelMapperService modelMapperService,
			EmployerService employerService, UserEntityService userEntityService,JobAdvertService jobAdvertService) {
		this.systemWorkerDao = systemWorkerDao;
		this.modelMapperService = modelMapperService;
		this.employerService = employerService;
		this.userEntityService = userEntityService;
		this.jobAdvertService = jobAdvertService;
		}

	@Override
	public Result add(CreateSystemWorkerRequest createSystemWorkerRequest) {

		checkIfEmailExists(createSystemWorkerRequest.getEmail());

		SystemWorker systemWorker = this.modelMapperService.forDto().map(createSystemWorkerRequest, SystemWorker.class);
		systemWorker.setRole("systemWorker");
		systemWorker.setValidation(true);
		this.systemWorkerDao.save(systemWorker);
		

		return new SuccessResult("Created SystemWorker");
	}

	@Override
	public Result delete(DeleteSystemWorkerRequest deleteSystemWorkerRequest) {

		checkIfSystemWorkerIdExists(deleteSystemWorkerRequest.getSystemWorkerId());
		this.systemWorkerDao.deleteById(deleteSystemWorkerRequest.getSystemWorkerId());
		return new SuccessResult("Deleted SystemWorker");
	}

	@Override
	public Result update(UpdateSystemWorkerRequest updateSystemWorkerRequest) {

		checkIfSystemWorkerIdExists(updateSystemWorkerRequest.getSystemWorkerId());

		return new SuccessResult("Updated SystemWorker");
	}

	@Override
	public DataResult<List<GetListSystemWorkerDto>> getAll() {

		List<SystemWorker> systemWorkers = this.systemWorkerDao.findAll();
		List<GetListSystemWorkerDto> response = systemWorkers.stream()
				.map(systemWorker -> this.modelMapperService.forDto().map(systemWorker, GetListSystemWorkerDto.class))
				.collect(Collectors.toList());

		return new SuccessDataResult<List<GetListSystemWorkerDto>>(response, "Listed SystemWorkers");
	}

	@Override
	public DataResult<GetListSystemWorkerDto> getBySystemWorkerId(int systemWorkerId) {

		checkIfSystemWorkerIdExists(systemWorkerId);

		SystemWorker systemWorker = this.systemWorkerDao.getById(systemWorkerId);
		GetListSystemWorkerDto response = this.modelMapperService.forDto().map(systemWorker,
				GetListSystemWorkerDto.class);
		return new SuccessDataResult<GetListSystemWorkerDto>(response, "Listed SystemWorker");
	}

	@Override
	public Result validateEmployerId(int id, boolean status) {

		this.employerService.validateEmployerId(id, status);
		return new SuccessResult("This employer has been verified");
	}

	@Override
	public Result jobStatusManuelChange(int jobId, boolean status) {

		this.jobAdvertService.jobStatusManuelChange(jobId, status);
		return new SuccessResult("Job Status changed");
	}

	private boolean checkIfSystemWorkerIdExists(int systemWorkerId) {

		if (this.systemWorkerDao.existsById(systemWorkerId)) {
			return true;
		}
		throw new BusinessException("This systemWorker id not found");
	}

	private boolean checkIfEmailExists(String email) {

		if (this.userEntityService.checkIfEmailExists(email)) {
			return true;
		}
		throw new BusinessException("This email already exists");

	}

}
