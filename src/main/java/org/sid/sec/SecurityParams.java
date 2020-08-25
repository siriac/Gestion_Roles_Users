package org.sid.sec;

public interface SecurityParams {
	public static final String JWT_HEADER_NAME="Authorization";
	public static final String SECRET="ulrichnwembe@gmail.com";
	public static final long EXPIRATION=10*24*3600;//c'est pas bien de laisser une formule comme ça il faut la calculer
	public static final String HEADER_PREFIX="Bearer ";

}
