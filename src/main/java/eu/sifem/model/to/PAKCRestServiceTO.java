
package eu.sifem.model.to;

public class PAKCRestServiceTO implements AbstractTO{

        private static final long serialVersionUID = -7622290983547686018L;

        private byte[] cfgFile;

        private byte[] datFile;

        private byte[] unvFile;
        
        private byte[] centerline;
        
        private byte[] imag;
        
        private byte[] real;
        
        private byte[] magn;
        
        private byte[] phase;        

        public byte[] getCenterline() {
            return centerline;
        }

        public void setCenterline(byte[] centerline) {
            this.centerline = centerline;
        }

        public byte[] getImag() {
            return imag;
        }

        public void setImag(byte[] imag) {
            this.imag = imag;
        }

        public byte[] getReal() {
            return real;
        }

        public void setReal(byte[] real) {
            this.real = real;
        }

        public byte[] getMagn() {
            return magn;
        }

        public void setMagn(byte[] magn) {
            this.magn = magn;
        }

        public byte[] getPhase() {
            return phase;
        }

        public void setPhase(byte[] phase) {
            this.phase = phase;
        }

        public byte[] getCfgFile() {
                return cfgFile;
        }

        public void setCfgFile(byte[] cfgFile) {
                this.cfgFile = cfgFile;
        }

        public byte[] getDatFile() {
                return datFile;
        }

        public void setDatFile(byte[] datFile) {
                this.datFile = datFile;
        }

        public byte[] getUnvFile() {
                return unvFile;
        }

        public void setUnvFile(byte[] unvFile) {
                this.unvFile = unvFile;
        }
}