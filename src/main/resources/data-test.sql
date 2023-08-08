

INSERT INTO `cabinet` (`id_cabinet`, `adresse`, `code_post`, `horaires`, `nom`) VALUES ('1', '1 rue de france', '10000', '2021-06-08 09:53:26.000000', 'Cabinet médical Sousou');


INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 1, 'apocair8@gmail.com', '$2a$10$Ns4iMhB/35DUG22CgrFCL.prNxOrQ3ggmThszl2L6y3fgXk5SwfX.', '', '0651214511' );
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 2, 'apocair7@gmail.com', '$2a$10$Ns4iMhB/35DUG22CgrFCL.prNxOrQ3ggmThszl2L6y3fgXk5SwfX.','',  '0651214511' );
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 3, 'apocair6@gmail.com', '$2a$10$Ns4iMhB/35DUG22CgrFCL.prNxOrQ3ggmThszl2L6y3fgXk5SwfX.','',  '0651214511' );
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 4, 'clinital@mail.com', '$2a$10$oCoo2EXvUXWhY0HShUhaa.6mScY4FCD5ik2iZeFxhhmnvCec/svF6', '',  '06666666');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 6, 'asta@gmail.com', '$2a$10$Ns4iMhB/35DUG22CgrFCL.prNxOrQ3ggmThszl2L6y3fgXk5SwfX.','',   '1233');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 7, 'takanum@lol.com', '$2a$10$AEUVGj6K1IxS/qr0J.iekOvhnGipvtGV3/P.2laqXSaCngA.7uVKC','',  '12345');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 8, 'blabla@mail.com', '$2a$10$G7jvbEAi/3qNSkEi/wNFKuSfqrzoxh0GOQmcYW5H2y.lKXZWir63q','',   '12345689');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 9, 'sante@mail.com', '$2a$10$e4Ihuy2KuVcn/ykd4amaNOptHmKJUvF/YBglu3DbJwIFolNu13ka.','',  '2345600');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 10, 'test@mail.com', '$2a$10$wfvDMRFfplosa/a1udQTbO7Yq6zMJuWKZQJOu0JOuP.DVsF1vdiuC','',  '028333');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 11, 'test1@mail.com', '$2a$10$NfVix81Kj3punYT3m7A5r.vqkuXFTQ.oNoAy0MFNyI6hiYojvjco2','',  '0123456');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 12, 'prog@mm.com', '$2a$10$LgzAE/LRTBV/8FWKMp2VqeTGCbFLVCHADPHaix6lK2C7Dm4/8/0bm', '',  '12346');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 13, '123@dhdh.com', '$2a$10$07sOrz2Zphmasz8tERrDFei7yCCXks6PpMm5CWfAWMX8ww1Gr3W2e','',  '123333');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 14, 'safouan@mail.com', '$2a$10$6/WSpJe30gSCmyp/NcGkqeij88fpOhOv4eZXfyXr1ehC.FDSF1qcy',  '', '12345678');
INSERT INTO `users` (`type_user`, `id`, `email`, `password`,  `email_verified`,  `telephone`) VALUES('User', 15, 'mona@gmail.com', '$2a$10$bfrPaO8Q7rzf/rqxaRuoPOv0rXARXMw0fOBSxz7z3EvBtXhzHyDuG','',  '0172727');
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(1, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(2, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(3, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(4, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(6, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(7, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(8, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(9, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(10, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(11, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(12, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(13, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(14, 1);
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES(15, 1);


INSERT INTO `dossiers` (`id_dossier`, `num_dossier`, `traitement`) VALUES (1, '1561581611', b'1');
INSERT INTO `dossiers` (`id_dossier`, `num_dossier`, `traitement`) VALUES (2, '1561581612', b'1');
INSERT INTO `dossiers` (`id_dossier`, `num_dossier`, `traitement`) VALUES (3, '1561581613', b'1');
INSERT INTO `dossiers` (`id_dossier`, `num_dossier`, `traitement`) VALUES (4, '1561581614', b'1');
INSERT INTO `dossiers` (`id_dossier`, `num_dossier`, `traitement`) VALUES (5, '1561581615', b'1');

INSERT INTO `medecins` (`civilite_med`, `description_med`, `diplome_med`, `experience_med`, `matricule_med`, `nom_med`, `photo_med`, `prenom_med`, `id`, `id_cabinet`, `specialite_id_spec`, `ville_id_ville`) VALUES ('Dr', 'Le meilleur docteur du monde', 'Université de Paris ', '5 Ans de pratique en tant que medecin généraliste dans l hopital americain à Paris', '1', 'Doudou', NULL, 'Simo', '1', '1', '1', '58');
INSERT INTO `medecins` (`civilite_med`, `description_med`, `diplome_med`, `experience_med`, `matricule_med`, `nom_med`, `photo_med`, `prenom_med`, `id`, `id_cabinet`, `specialite_id_spec`, `ville_id_ville`) VALUES ('Dr', 'la meilleure', 'Université de Paris ', '5 Ans de pratique en tant que medecin généraliste dans l hopital americain à Paris', '2', 'Soussi', NULL, 'bsibisa', '3', '1', '2', '252');


INSERT INTO `patients` (`adresse_pat`, `civilite_pat`, `code_post_pat`, `date_naissance`, `matricule_pat`, `nom_pat`, `prenom_pat`, `id`, `dossier_medical_id_dossier`, `id_ville`, `id_user`, `patient_type`) VALUES ('A rue baakili', 'MR', '10000', '2021-07-05 23:25:22.000000', '1', 'Diouri', 'Youssef', '6', '1', '58','1','MOI');
INSERT INTO `rendezvous` (`id`, `jour`, `motif`, `medecin`, `patient`) VALUES ('1', '2021-06-03 12:08:23.000000', 'Consultation', '3', '6');

INSERT INTO `documents` (`id_doc`, `auteur`, `date_ajout_doc`, `fichier_doc`, `numero_doc`, `titre_doc`, `id_typedoc`, `id_dossier`, `patient_id`) VALUES
(1, 'Medecin sousou', '2021-06-22 10:22:35.000000', NULL, NULL, 'Ordonance', 1, 1, 6);
