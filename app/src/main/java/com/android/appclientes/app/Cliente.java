package com.android.appclientes.app;

public class Cliente {
	int id;
	String CPFCNPJ;
	String nome;
	String email;
	String Fone;
	String Celular; 
	String Contato;
	String Endereco;
	String CEP;
	String Cidade;
	Double Latitude;
	Double Longitude;

	
	public void setId(int id) {
		this.id = id;
	}
	public void setCPFCNPJ(String cPFCNPJ) {
		CPFCNPJ = cPFCNPJ;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setFone(String fone) {
		Fone = fone;
	}
	public void setCelular(String celular) {
		Celular = celular;
	}
	public void setContato(String contato) {
		Contato = contato;
	}
	public void setEndereco(String endereco) {
		Endereco = endereco;
	}
	public void setCEP(String cEP) {
		CEP = cEP;
	}
	public void setCidade(String cidade) {
		Cidade = cidade;
	}
	

	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}
	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}
	public int getId() {
		return id;
	}
	public String getCPFCNPJ() {
		return CPFCNPJ;
	}
	public String getNome() {
		return nome;
	}
	public String getEmail() {
		return email;
	}
	public String getFone() {
		return Fone;
	}
	public String getCelular() {
		return Celular;
	}
	public String getContato() {
		return Contato;
	}
	public String getEndereco() {
		return Endereco;
	}
	public String getCEP() {
		return CEP;
	}
	public String getCidade() {
		return Cidade;
	}
	
	
	public Double getLatitude() {
		return Latitude;
	}
	public Double getLongitude() {
		return Longitude;
	}
	public String toString(){
		return getNome();
		
	}
	
}
