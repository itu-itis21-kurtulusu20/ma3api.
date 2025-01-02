package tr.gov.tubitak.uekae.esya.api.infra.certstore.db.cekirdek.yardimci;

import tr.gov.tubitak.uekae.esya.api.common.ESYARuntimeException;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.db.katman.Islemler;
import tr.gov.tubitak.uekae.esya.api.infra.certstore.model.*;


/**
 * @author ahmety
 */
public class DepoIslemleri extends Islemler
{

    public DepoIslemleri()
    {
        register(DepoDizin.class, new DizinYardimci());
        register(DepoSIL.class, new SILYardimci());
        register(DepoSertifika.class, new SertifikaYardimci());
        register(DepoKokSertifika.class, new KokSertifikaYardimci());
        register(DizinSILYardimci.class, new DizinSILYardimci());
        register(DizinSertifikaYardimci.class, new DizinSertifikaYardimci());
        register(DepoSistemParametresi.class, new SistemParametresiYardimci());
        register(DepoSilinecekKokSertifika.class, new SilinecekKokSertifikaYardimci());
        register(DepoOzet.class, new OzetYardimci());
        register(DepoOCSP.class, new OCSPYardimci());
        register(DepoOCSPToWrite.class, new OCSPYardimci());
        register(SertifikaOCSPsYardimci.class, new SertifikaOCSPsYardimci());
        register(DepoNitelikSertifikasi.class, new NitelikSertifikasiYardimci());
    }

    protected void setVtId(Object aModelNesnesi, Long aId)
    {
        if (aModelNesnesi instanceof DepoDizin){
            ((DepoDizin)aModelNesnesi).setDizinNo(aId);
        }
        else if (aModelNesnesi instanceof DepoSIL) {
            ((DepoSIL)aModelNesnesi).setSILNo(aId);
        }
        else if (aModelNesnesi instanceof DepoSertifika) {
            ((DepoSertifika)aModelNesnesi).setSertifikaNo(aId);
        }
        else if (aModelNesnesi instanceof DepoKokSertifika) {
            ((DepoKokSertifika)aModelNesnesi).setKokSertifikaNo(aId);
        }
        else if (aModelNesnesi instanceof DepoSilinecekKokSertifika) {
            ((DepoSilinecekKokSertifika)aModelNesnesi).setKokSertifikaNo(aId);
        }
        else if (aModelNesnesi instanceof DepoOzet) {
            ((DepoOzet)aModelNesnesi).setHashNo(aId);
        }
        else if (aModelNesnesi instanceof DepoOCSP) {
            ((DepoOCSP)aModelNesnesi).setOCSPNo(aId);
        }
        else if (aModelNesnesi instanceof DepoNitelikSertifikasi) {
            ((DepoNitelikSertifikasi)aModelNesnesi).setNitelikSertifikaNo(aId);
        }
        else throw new ESYARuntimeException("Nesne id set etme yontemi tanimlanmamis! : "+aModelNesnesi.getClass());
    }

    protected Long getVtId(Object aModelNesnesi)
    {
        if (aModelNesnesi instanceof DepoDizin){
            return ((DepoDizin)aModelNesnesi).getDizinNo();
        }
        else if (aModelNesnesi instanceof DepoSIL) {
            return ((DepoSIL)aModelNesnesi).getSILNo();
        }
        else if (aModelNesnesi instanceof DepoSertifika) {
            return ((DepoSertifika)aModelNesnesi).getSertifikaNo();
        }
        else if (aModelNesnesi instanceof DepoKokSertifika) {
            return ((DepoKokSertifika)aModelNesnesi).getKokSertifikaNo();
        }
        else if (aModelNesnesi instanceof DepoSilinecekKokSertifika) {
            return ((DepoSilinecekKokSertifika)aModelNesnesi).getKokSertifikaNo();
        }
        else if (aModelNesnesi instanceof DepoOzet) {
            return ((DepoOzet)aModelNesnesi).getHashNo();
        }
        
        else if (aModelNesnesi instanceof DepoOCSP) {
            return ((DepoOCSP)aModelNesnesi).getOCSPNo();
        }
        
        else if (aModelNesnesi instanceof DepoNitelikSertifikasi) {
            return ((DepoNitelikSertifikasi)aModelNesnesi).getNitelikSertifikaNo();
        }
        else throw new ESYARuntimeException("Nesne id get etme yontemi tanimlanmamis! : "+aModelNesnesi.getClass());
    }
}
