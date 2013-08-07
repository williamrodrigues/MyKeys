package br.schoollabs.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import br.schoollabs.utils.DateDiffUtil.Units;

@SuppressLint("SimpleDateFormat")
public class DataSistema {
	private static final SimpleDateFormat dataPattern = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat dataTimePattern = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	private static final SimpleDateFormat dataTimeSecondsPattern = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private static final SimpleDateFormat dataTimeSecondsMilisPattern = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
	private static final SimpleDateFormat dataTimeSecondsMilisPatternNotCaracter = new SimpleDateFormat("ddMMyyyyHHmmssSSS");

	/**
	 * Compara a data de aniversario com a data atual, verificando com margem de diasPos para frente
	 * 
	 * @param dtNasc
	 * @param diasPos
	 * @return
	 */
	@SuppressWarnings("deprecation")
	static public boolean compareAniversario(Date dtNasc, int diasPos) {
		if (dtNasc == null)
			return false;

		Date dataAtual = new Date();

		Date dtNascN = new Date(dtNasc.getTime());
		dtNascN.setYear(dataAtual.getYear());

		long diff = DateDiffUtil.dateDiff(dtNascN, dataAtual, Units.DAY);
		return (diff >= 0 && diff <= diasPos);
	}

	@SuppressWarnings("deprecation")
	static public boolean compareDDMMYYYY(Date d1, Date d2) {
		if (d1 == null || d2 == null)
			return false;

		if (d1.getDay() != d2.getDay())
			return false;
		if (d1.getMonth() != d2.getMonth())
			return false;
		if (d1.getYear() != d2.getYear())
			return false;
		return true;
	}

	static public Date getDate(String dateDDMMYYYY) {
		try {
			return new SimpleDateFormat("dd/MM/yyyy").parse(dateDDMMYYYY);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Retorna um objeto Date.
	 * 
	 * @param date
	 *            formato 10/10/2010 10:10:10
	 * @return
	 */
	static public Date getDateHourMinSecs(String date) {
		try {
			return dataTimeSecondsPattern.parse(date);
		} catch (Exception e) {
			return null;
		}
	}

	static public Date getInfiniteDate() {
		return getDate("12/31/3000");
	}

	static public String getDataTimeCorrenteDDMMYYYYHHMM() {
		return dataTimePattern.format(new Date());
	}

	static public String getDataTimeCorrenteDDMMYYYYHHMMSSSSS() {
		return dataTimeSecondsMilisPatternNotCaracter.format(new Date());
	}

	static public String getDataCorrenteDDMMYYYY() {
		Date dt = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		return sd.format(dt);
	}

	static public String formataDataDDMMYY(Date dt) {
		SimpleDateFormat sd = new SimpleDateFormat("ddMMyy");
		return sd.format(dt);
	}

	/**
	 * Transforma uma String no formato dd/MM/yyyy HH:mm em data
	 * 
	 * @param dataHora
	 * @return
	 */
	static public Date parseDateTime(String dataHora) {
		try {
			return dataTimePattern.parse(dataHora);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Formata a data com o pattern dd/MM/yyyy
	 * 
	 * @param dt
	 *            data a ser formatada
	 * @return Uma string com a data formatada
	 */
	static public String formataData(Date dt) {
		return dataPattern.format(dt);
	}

	static public String getDataCorrenteExtenso() {
		Date dt = new Date();
		DateFormat dtSaida = DateFormat.getDateInstance(DateFormat.LONG);
		return dtSaida.format(dt);
	}

	static public Timestamp getDataCorrenteTimestamp() {
		Timestamp tm = new Timestamp(Calendar.getInstance().getTimeInMillis());
		return tm;
	}

	static public Date getDataCorrenteDate() {
		Date d = new Date(Calendar.getInstance().getTimeInMillis());
		return d;
	}

	static public Timestamp getDataDDMMYYYtoTimestamp(String dataDDMMYYYY) {
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		Date dt = new Date();
		try {
			dt = sd.parse(dataDDMMYYYY);
		} catch (ParseException ex) {
			System.out.println("Erro na conversao de data sera retornada a data corrente!");
		}
		Timestamp tm = new Timestamp(dt.getTime());
		return tm;
	}

	static public Timestamp getDatatoTimestamp(Date data) {
		Timestamp tm;
		try {
			tm = new Timestamp(data.getTime());
		} catch (Exception e) {
			tm = null;
		}
		return tm;
	}

	static public Date getTimestamptoDate(Timestamp dataHora) {
		Date dt;
		try {
			dt = new Date(dataHora.getTime());
		} catch (Exception e) {
			dt = null;
		}
		return dt;
	}

	static public int getDateYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
		// Date dt = new Date();
		// SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		// String datas = sd.format(dt);
		//
		// return Integer.parseInt(datas.substring(6, 10));
	}

	static public int getDateMonth() {
		Date dt = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		String datas = sd.format(dt);

		return Integer.parseInt(datas.substring(3, 5));
	}

	static public int getDateDay() {
		Date dt = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		String datas = sd.format(dt);

		return Integer.parseInt(datas.substring(0, 2));
	}

	static public String parseTimestampString(Timestamp tm) {
		Date dt = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		dt = (Date) tm.clone();
		return sd.format(dt);
	}

	static public String parseTimeStamptoString(Timestamp tm) {
		Date dt = new Date();
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dt = (Date) tm.clone();
		return sd.format(dt);
	}

	static public Timestamp parseStringTimestamp(String dataddMMyyyyHHmmss) {
		// "dd/MM/yyyy HH:mm:ss"
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date dt = new Date();
		try {
			dt = sd.parse(dataddMMyyyyHHmmss);
		} catch (ParseException ex) {
			System.out.println("Erro na conversao de data sera retornada a data corrente!");
		}
		Timestamp tm = new Timestamp(dt.getTime());
		return tm;
	}

	/**
	 * Transforma uma data no formato dd/MM/yyyy HH:mm:ss:SSS
	 * 
	 * @param dataddMMyyyyHHmmss
	 * @return
	 */
	static public Timestamp parseStringTimestampMilis(String dataddMMyyyyHHmmss) {
		// "dd/MM/yyyy HH:mm:ss"

		Date dt = new Date();
		try {
			dt = dataTimeSecondsMilisPattern.parse(dataddMMyyyyHHmmss);
		} catch (ParseException ex) {
			System.out.println("Erro na conversao de data sera retornada a data corrente!");
		}
		Timestamp tm = new Timestamp(dt.getTime());
		return tm;
	}

	static public String parseDateString(Date data) {
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		return sd.format(data);
	}

	static public Date parseStringToDate(String data) {
		try {
			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
			return sd.parse(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static public Date parseStringToDate2(String data) {
		try {
			SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
			return sd.parse(data);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static public Timestamp addMinute(Timestamp tm, int minute) {
		Calendar c = Calendar.getInstance();
		c.setTime(tm);
		c.add(Calendar.MINUTE, minute);
		return new Timestamp(c.getTimeInMillis());
	}

	static public Timestamp addHora(Timestamp tm, int hora) {
		Calendar c = Calendar.getInstance();
		c.setTime(tm);
		c.add(Calendar.HOUR, hora);
		return new Timestamp(c.getTimeInMillis());
	}

	static public Date addMes(Date tm, int mes) {
		Calendar c = Calendar.getInstance();
		c.setTime(tm);
		c.add(Calendar.MONTH, mes);
		return new Date(c.getTimeInMillis());
	}

	static public Date addDia(Date tm, int dia) {
		Calendar c = Calendar.getInstance();
		c.setTime(tm);
		c.add(Calendar.DAY_OF_MONTH, dia);
		return new Date(c.getTimeInMillis());
	}

	static public Date addSegundo(Date tm, int segundo) {
		Calendar c = Calendar.getInstance();
		c.setTime(tm);
		c.add(Calendar.SECOND, segundo);
		return new Date(c.getTimeInMillis());
	}

	static public Date addAno(Date tm, int ano) {
		Calendar c = Calendar.getInstance();
		c.setTime(tm);
		c.add(Calendar.YEAR, ano);
		return new Date(c.getTimeInMillis());
	}

	public static String formatDate(Date date, String format) {
		try {
			SimpleDateFormat sd = new SimpleDateFormat(format);
			return sd.format(date);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Converte uma String para um objeto Date. Caso a String seja vazia ou nula, retorna null - para facilitar em casos onde formularios podem ter campos de datas vazios.
	 * 
	 * @param data
	 *            String no formato dd/MM/yyyy a ser formatada
	 * @return Date Objeto Date ou null caso receba uma String vazia ou nula
	 * @throws Exception
	 *             Caso a String esteja no formato errado
	 */
	public static java.util.Date convertStringAsDate(String data) throws Exception {
		if (data == null || data.equals(""))
			return null;

		java.util.Date date = null;
		try {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			date = new java.sql.Date(((java.util.Date) formatter.parse(data)).getTime());
		} catch (ParseException e) {
			throw e;
		}
		return date;
	}

	/**
	 * Converte um Long para uma String no formato de minutos mm:ss.
	 * 
	 * @param minutos
	 *            Long com os minutos a ser formatado
	 * @return String formatada mm:ss
	 */
	public static String segundoToMinuto(Long segundos) {
		String saida;
		if (segundos > 0) {
			Long divisao = segundos / 60;
			Long resto = segundos % 60;
			saida = divisao + ":";
			if (resto > 0) {
				saida = saida + resto;
			} else {
				saida = saida + "00";
			}
		} else {
			saida = "0:00";
		}
		return saida;
	}

	public static Date setHoraMinuto(Date date, int hora, int minuto) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hora);
		c.set(Calendar.MINUTE, minuto);
		return c.getTime();
	}

	/**
	 * Retorna o ultimo dia do mes/ano
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 */
	public static int getUltimoDiaMes(int ano, int mes) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.MONTH, mes - 1);
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Retorna uma data com o ultimo dia do mes/ano informado, com hora 23:59:59
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 */
	public static Date getUltimoDiaMesDate(int ano, int mes) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.MONTH, mes - 1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	/**
	 * Retorna uma data com o primeiro dia do mes/ano informado, com hora 00:00:00
	 * 
	 * @param ano
	 * @param mes
	 * @return
	 */
	public static Date getPrimeiroDiaMesDate(int ano, int mes) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.MONTH, mes - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date getUltimoDiaSemanaDate(int ano, int mes, int semana) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.MONTH, mes - 1);

		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.set(Calendar.WEEK_OF_MONTH, semana);
		cal.set(Calendar.DAY_OF_WEEK, 7);
		// cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static Date getPrimeiroDiaSemanaDate(int ano, int mes, int semana) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.MONTH, mes - 1);

		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.set(Calendar.WEEK_OF_MONTH, semana);
		cal.set(Calendar.DAY_OF_WEEK, 1);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date getPrimeiroDiaSemanaDate(int ano, int semanaDoAno) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.WEEK_OF_YEAR, semanaDoAno);

		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.set(Calendar.DAY_OF_WEEK, 1);

		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date getUltimoDiaSemanaDate(int ano, int semanaDoAno) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);

		cal.setFirstDayOfWeek(Calendar.SUNDAY);
		cal.set(Calendar.WEEK_OF_YEAR, semanaDoAno);
		cal.set(Calendar.DAY_OF_WEEK, 7);

		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static Date getDataInicio(int ano, int mes, int semanaDoMes, int semanaDoAno) {
		if (semanaDoMes > 0) { // Busca pela semana do ano/mes
			return DataSistema.getPrimeiroDiaSemanaDate(ano, mes, semanaDoMes);
		} else if (semanaDoAno > 0) { // Busca pela semana do ano
			return DataSistema.getPrimeiroDiaSemanaDate(ano, semanaDoAno);
		} else {// Busca pelo ano/mes
			return DataSistema.getPrimeiroDiaMesDate(ano, mes);
		}
	}

	public static Date getDataFim(int ano, int mes, int semanaDoMes, int semanaDoAno) {
		if (semanaDoMes > 0) { // Busca pela semana do ano/mes
			return DataSistema.getUltimoDiaSemanaDate(ano, mes, semanaDoMes);
		} else if (semanaDoAno > 0) { // Busca pela semana do ano
			return DataSistema.getUltimoDiaSemanaDate(ano, semanaDoAno);
		} else {// Busca pelo ano/mes
			return DataSistema.getUltimoDiaMesDate(ano, mes);
		}
	}

	/**
	 * Retorna a semana do ano em que a data se encontra
	 * 
	 * @param data
	 * @return
	 */
	public static int getSemanaDoAno(Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	public static int getSemanaAtualDoAno() {
		return getSemanaDoAno(new Date());
	}

	public static void main(String args[]) {
		// System.out.println(getPrimeiroDiaSemanaDate(2013, 4, 5));
		// System.out.println(getUltimoDiaSemanaDate(2013, 4, 5));
		// int semanaAtual = getSemanaDoAno(new Date());
		// System.out.println(getDataInicio(2013, 0, 0, 16));
		// System.out.println(getUltimoDiaSemanaDate(2013, semanaAtual));
		System.out.println(getDataTimeCorrenteDDMMYYYYHHMMSSSSS());

		// System.out.println(semanaAtual);
	}
}
