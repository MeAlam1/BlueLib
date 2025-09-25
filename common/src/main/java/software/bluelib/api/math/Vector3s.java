package software.bluelib.api.math;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class Vector3s implements Externalizable, Cloneable {
	public String x;
	public String y;
	public String z;

	public Vector3s(String pX, String pY, String pZ) {
		this.x = pX;
		this.y = pY;
		this.z = pZ;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}

	public void writeExternal(ObjectOutput pOut) throws IOException {
		pOut.writeUTF(x);
		pOut.writeUTF(y);
		pOut.writeUTF(z);
	}

	public void readExternal(ObjectInput pIn) throws IOException {
		set(pIn.readUTF(), pIn.readUTF(), pIn.readUTF());
	}

	public Vector3s set(String pX, String pY, String pZ) {
		this.x = pX;
		this.y = pY;
		this.z = pZ;
		return this;
	}

	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}

