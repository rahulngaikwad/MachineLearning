package mlproject1.id3;

import java.util.List;

class Instance{
	private Integer instanceId ;
	private List<Integer> attributes;
	private Integer targetAttribute;
		
	public Instance() {
		this.instanceId = null;
		this.attributes = null;
		this.targetAttribute = null;
	}

	public Instance(Integer instanceId, List<Integer> attributes,
			Integer targetAttribute) {
		this.instanceId = instanceId;
		this.attributes = attributes;
		this.targetAttribute = targetAttribute;
	}

	public Integer getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}

	public List<Integer> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Integer> attributes) {
		this.attributes = attributes;
	}

	public Integer getTargetAttribute() {
		return targetAttribute;
	}

	public void setTargetAttribute(Integer targetAttribute) {
		this.targetAttribute = targetAttribute;
	}

	@Override
	public String toString() {
		return "Instance [id=" + instanceId + ", attributes="
				+ attributes + ", T=" + targetAttribute + "]";
	}	
	
	
}