package com.android.appclientes.app;

public class Produto {
	int id;
	String nome;
	
	
	public int getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String toString(){
		return getNome();
		
	}
	
}
