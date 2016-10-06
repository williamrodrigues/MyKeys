package br.schoollabs.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

import android.annotation.SuppressLint;
import android.util.Base64;

@SuppressWarnings("resource")
@SuppressLint({ "SdCardPath", "TrulyRandom" })
public class RSA {
	public static final String ALGORITHM = "RSA";

	/**
	 * Local da chave privada no sistema de arquivos.
	 */
	public static final String PATH_CHAVE_PRIVADA = "/data/data/br.schoollabs.mykeys/keys/private.key";

	/**
	 * Local da chave pública no sistema de arquivos.
	 */
	public static final String PATH_CHAVE_PUBLICA = "/data/data/br.schoollabs.mykeys/keys/public.key";

	/**
	 * Gera a chave que contém um par de chave Privada e Pública usando 1025
	 * bytes. Armazena o conjunto de chaves nos arquivos private.key e
	 * public.key
	 */
	@SuppressLint("TrulyRandom")
	public static void geraChave() {
		try {
			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
			keyGen.initialize(1024);
			final KeyPair key = keyGen.generateKeyPair();

			File chavePrivadaFile = new File(PATH_CHAVE_PRIVADA);
			File chavePublicaFile = new File(PATH_CHAVE_PUBLICA);

			// Cria os arquivos para armazenar a chave Privada e a chave Publica
			if (chavePrivadaFile.getParentFile() != null) {
				chavePrivadaFile.getParentFile().mkdirs();
			}

			chavePrivadaFile.createNewFile();

			if (chavePublicaFile.getParentFile() != null) {
				chavePublicaFile.getParentFile().mkdirs();
			}

			chavePublicaFile.createNewFile();

			// Salva a Chave Pública no arquivo
			ObjectOutputStream chavePublicaOS = new ObjectOutputStream(new FileOutputStream(chavePublicaFile));
			chavePublicaOS.writeObject(key.getPublic());
			chavePublicaOS.close();

			// Salva a Chave Privada no arquivo
			ObjectOutputStream chavePrivadaOS = new ObjectOutputStream(new FileOutputStream(chavePrivadaFile));
			chavePrivadaOS.writeObject(key.getPrivate());
			chavePrivadaOS.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Verifica se o par de chaves Pública e Privada já foram geradas.
	 */
	public static boolean verificaSeExisteChavesNoSO() {

		File chavePrivada = new File(PATH_CHAVE_PRIVADA);
		File chavePublica = new File(PATH_CHAVE_PUBLICA);

		if (chavePrivada.exists() && chavePublica.exists()) {
			return true;
		}

		return false;
	}

	/**
	 * Criptografa o texto puro usando chave pública.
	 */
	public static byte[] criptografa(String texto, PublicKey chave) {
		byte[] cipherText = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// Criptografa o texto puro usando a chave Púlica
			cipher.init(Cipher.ENCRYPT_MODE, chave);
			cipherText = cipher.doFinal(texto.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cipherText;
	}

	/**
	 * Decriptografa o texto puro usando chave privada.
	 */
	public static String decriptografa(byte[] texto, PrivateKey chave) {
		byte[] dectyptedText = null;

		try {
			final Cipher cipher = Cipher.getInstance(ALGORITHM);
			// Decriptografa o texto puro usando a chave Privada
			cipher.init(Cipher.DECRYPT_MODE, chave);
			dectyptedText = cipher.doFinal(texto);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new String(dectyptedText);
	}

	public static PublicKey publicKey() {
		try {
			return (PublicKey) (new ObjectInputStream(new FileInputStream(PATH_CHAVE_PUBLICA)).readObject());
		} catch (Exception e) {
			return null;
		}
    }
	
	public static PrivateKey privateKey() {
		try {
			return (PrivateKey) (new ObjectInputStream(new FileInputStream(PATH_CHAVE_PRIVADA)).readObject());
		} catch (Exception e) {
			return null;
		}
    }
	
	public static String encrypt(String value) {
		return Base64.encodeToString(criptografa(value, publicKey()), 2045);
	}
	
	public static String decrypter(String value) {
		return decriptografa(Base64.decode(value, 2045), privateKey());
	}
}
