package com.example.diploma_work;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        // Создаем объект для использования алгоритма SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // Преобразуем пароль в байтовый массив и передаем его объекту MessageDigest
        md.update(password.getBytes());
        // Получаем хэш-значение в байтах
        byte[] hash = md.digest();
        // Преобразуем байты хэш-значения в шестнадцатеричную строку
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        // Возвращаем шестнадцатеричную строку хэш-значения
        return hexString.toString();
    }
}
