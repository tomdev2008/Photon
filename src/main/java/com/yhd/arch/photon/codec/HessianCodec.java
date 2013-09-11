package com.yhd.arch.photon.codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import akka.serialization.JSerializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

public class HessianCodec extends JSerializer {

	public int identifier() {
		return 10017;
	}

	public boolean includeManifest() {
		return false;
	}

	public byte[] toBinary(Object arg0) {
		byte[] arr = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream(5120);
		Hessian2Output ho = new Hessian2Output(bos);
		try {
			ho.startMessage();
			ho.writeObject(arg0);
			ho.completeMessage();
			ho.close();
			arr = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
			}
		}
		return arr;
	}

	@Override
	public Object fromBinaryJava(byte[] arg0, Class<?> arg1) {
		Object o = null;
		ByteArrayInputStream bis = new ByteArrayInputStream(arg0);
		Hessian2Input in = new Hessian2Input(bis);
		try {
			in.startMessage();
			o = in.readObject();
			in.completeMessage();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return o;
	}

}
