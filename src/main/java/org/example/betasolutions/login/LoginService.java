package org.example.betasolutions.login;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private final LoginRepository loginRepository;

    public LoginService(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;

    }

    public boolean createLogin(Login login) {
         return loginRepository.createLogin(login);
    }
    public boolean verifyLogin(Login login) {
        return loginRepository.verifyLogin(login);
    }

}

