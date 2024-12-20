package dijairdev.com.br.retrofit;

import dijairdev.com.br.retrofit.cep.CepService;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitConfig {
    private final Retrofit retrofit;

    public RetrofitConfig() {
        retrofit = new Retrofit.Builder().baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    public CepService getCepService() {
        return this.retrofit.create(CepService.class);
    }
}
