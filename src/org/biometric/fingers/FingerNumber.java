package org.biometric.fingers;

public enum FingerNumber {
	UNKNOWN_FINGER(0x00), RIGHT_THUMB(0x01), RIGHT_INDEX(0x02), RIGHT_MIDDLE(0x03), RIGHT_RING(0x04), RIGHT_LITTLE(
			0x05), LEFT_THUMB(0x06), LEFT_INDEX(0x07), LEFT_MIDDLE(0x08), LEFT_RING(0x09), LEFT_LITTLE(0x0A);

	int finger;

	FingerNumber(int p) {
		finger = p;
	}

	int showFingerNumber() {
		return finger;
	}
}
