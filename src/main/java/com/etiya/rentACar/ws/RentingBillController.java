package com.etiya.rentACar.ws;

import java.sql.Date;
import java.util.List;

import com.etiya.rentACar.business.request.rentingBillRequests.CreateRentingBillRequest;
import com.etiya.rentACar.business.request.rentingBillRequests.DeleteRentingBillRequest;
import com.etiya.rentACar.business.request.rentingBillRequests.UpdateRentingBillRequest;
import com.etiya.rentACar.core.utilities.results.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.etiya.rentACar.business.abstracts.RentingBillService;
import com.etiya.rentACar.business.dtos.RentingBillSearchListDto;
import com.etiya.rentACar.core.utilities.results.DataResult;

import javax.validation.Valid;

@RestController
@RequestMapping("api/rentingBills")
public class RentingBillController {
	private RentingBillService rentingBillService;

	@Autowired
	public RentingBillController(RentingBillService rentingBillService) {
		super();
		this.rentingBillService = rentingBillService;
	}
	
	@GetMapping("list")
	public DataResult<List<RentingBillSearchListDto>> getAll(){
		return this.rentingBillService.getAll();
	}
	@GetMapping("getBillsByUserId")
	public DataResult<List<RentingBillSearchListDto>> getBillsByUserId(@RequestParam int userId){
		return this.rentingBillService.getRentingBillByUserId(userId);
	}
	@GetMapping("getBillsBetweenDates")
	public DataResult<List<RentingBillSearchListDto>> getBillsBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate){
		return this.rentingBillService.getRentingBillByDateInterval(startDate, endDate);
	}
	@PostMapping("add")
	public Result save(@RequestBody @Valid CreateRentingBillRequest createRentingBillRequest){
		return this.rentingBillService.save(createRentingBillRequest);
	}
	@DeleteMapping("delete")
	public Result delete(@RequestBody @Valid DeleteRentingBillRequest deleteRentingBillRequest){
		return this.rentingBillService.delete(deleteRentingBillRequest);
	}
	@PutMapping("update")
	public Result update(@RequestBody @Valid UpdateRentingBillRequest updateRentingBillRequest){
		return this.rentingBillService.update(updateRentingBillRequest);
	}
}
