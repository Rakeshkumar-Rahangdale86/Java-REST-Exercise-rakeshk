package request.pojos;

public class ReqPatchDepositPaid {
 
	private Boolean depositpaid;

	public ReqPatchDepositPaid() {
		
	}
	
	public ReqPatchDepositPaid(Boolean depositpaid) {
		this.depositpaid = depositpaid;
	}

	public Boolean getDepositpaid() {
		return depositpaid;
	}

	public void setDepositpaid(Boolean depositpaid) {
		this.depositpaid = depositpaid;
	}
	
	
	
}
